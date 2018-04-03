package services.impl;

import dao.CoffeeOrderDao;
import dao.CoffeeOrderItemDao;
import dao.CoffeeTypeDao;
import dao.ConfigurationDao;
import entities.CoffeeOrder;
import entities.CoffeeOrderItem;
import entities.CoffeeType;
import org.apache.log4j.Logger;
import services.CoffeeOrderService;
import services.ConfigurationService;
import services.ServiceException;
import utils.SingletonBuilder;
import vo.CoffeeOrderAndCost;
import vo.Cost;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class CoffeeOrderServiceImpl extends AbstractService implements CoffeeOrderService {
    private static Logger log = Logger.getLogger(CoffeeOrderServiceImpl.class);

    private CoffeeOrderDao coffeeOrderDao = SingletonBuilder.getInstanceImpl(CoffeeOrderDao.class);
    private CoffeeOrderItemDao coffeeOrderItemDao = SingletonBuilder.getInstanceImpl(CoffeeOrderItemDao.class);
    private CoffeeTypeDao coffeeTypeDao = SingletonBuilder.getInstanceImpl(CoffeeTypeDao.class);
    private ConfigurationDao configurationDao = SingletonBuilder.getInstanceImpl(ConfigurationDao.class);
    private ConfigurationService configurationService = SingletonBuilder.getInstanceImpl(ConfigurationService.class);

    private CoffeeOrderServiceImpl() {}

    /**
     * Forms an order for not empty list of order elements and deliveryAddress != null,
     * calculates the cost of delivery and ordered coffee. If the formation is successful,
     * it saves the order and the list of order items in the database
     *
     * @param customerName        determines the name of the customer
     * @param deliveryAddress     determines the delivery address
     * @param coffeeOrderItemList list of order elements
     * @return an CoffeeOrderAndCost object that contains order stored in the database and object
     *         Cost that contains cost of delivery and ordered coffee,
     *         or return null if the order is not generated
     */
    @Override
    public CoffeeOrderAndCost makeOrder(String customerName, String deliveryAddress,
                                        List<CoffeeOrderItem> coffeeOrderItemList) {
        if (deliveryAddress != null && coffeeOrderItemList != null &&
                ! coffeeOrderItemList.isEmpty()) {
            CoffeeOrder coffeeOrder = new CoffeeOrder();
            try {
                this.startTransaction();
                // form Order data & save in Db
                Date currentDate = new java.util.Date();
                java.sql.Timestamp timestamp = new java.sql.Timestamp(currentDate.getTime());
                coffeeOrder.setOrderDate(timestamp);
                coffeeOrder.setCustomerName(customerName);
                coffeeOrder.setDeliveryAddress(deliveryAddress);
                Cost cost = costCalculate(coffeeOrderItemList);
                coffeeOrder.setCost(cost.getCoffeeTotalCost() + cost.getDeliveryCost());
                coffeeOrder = coffeeOrderDao.save(coffeeOrder);

                // save Order Items in Db
                for (CoffeeOrderItem item : coffeeOrderItemList) {
                    item.setOrderId(coffeeOrder.getId());
                    coffeeOrderItemDao.save(item);
                }
                this.commit();
                this.stopTransaction();

                CoffeeOrderAndCost coffeeOrderAndCost = new CoffeeOrderAndCost();
                coffeeOrderAndCost.setCoffeeOrder(coffeeOrder);
                coffeeOrderAndCost.setCost(cost);

                return coffeeOrderAndCost;
            } catch (SQLException e) {
                rollback();
                String errorMessage = "Error saving CoffeeOrder: " + coffeeOrder;
                log.error(errorMessage + e.getMessage());
                throw new ServiceException(errorMessage);
            }
        } else {
            return null;
        }

    }

    /**
     * returns an CoffeeOrder record with an id = coffeeOrderId from the database
     *
     * @param coffeeOrderId determine id of CoffeeOrder record in database
     * @return CoffeeOrder record from the database with id = coffeeOrderId, or
     * null if such an entity was not found
     */
    @Override
    public CoffeeOrder get(Serializable coffeeOrderId) {
        try {
            return coffeeOrderDao.get(coffeeOrderId);
        } catch (SQLException e) {
            String errorMessage = "Error getting CoffeeOrder by id: " + coffeeOrderId;
            log.error(errorMessage + e.getMessage());
            throw new ServiceException(errorMessage);
        }
    }

    /**
     * removes from the database an CoffeeOrder entity with an id = coffeeOrderId
     * and all CoffeeOrderItems where CoffeeOrderItem.orderId = coffeeOrderId
     *
     * @param coffeeOrderId determine id of entity in database
     * @return returns the number of deleted CoffeeOrder rows from the database
     */
    @Override
    public int delete(Serializable coffeeOrderId) {
        try {
            this.startTransaction();
            List<CoffeeOrderItem> coffeeOrderItemList = coffeeOrderItemDao.getAllForOrderId(coffeeOrderId);
            for (CoffeeOrderItem item : coffeeOrderItemList) {
                coffeeOrderItemDao.delete(item.getId());
            }
            int deletedRecords = coffeeOrderDao.delete(coffeeOrderId);
            this.commit();
            this.stopTransaction();

            return deletedRecords;
        } catch (SQLException e) {
            rollback();
            String errorMessage = "Error deleting from DB CoffeeOrder with id: " + coffeeOrderId;
            log.error(errorMessage + e.getMessage());
            throw new ServiceException(errorMessage);
        }
    }

    private Cost costCalculate(List<CoffeeOrderItem> coffeeOrderItemList) throws SQLException {

        // take configuration for free cup
        String parameterN = configurationService.getValue("n");
        int freeCupN;
        if (parameterN == null) {
            throw new SQLException("There is no default configuration or configuration " +
                    "stored in the database for the key: [n]");
        } else {
            freeCupN = Integer.valueOf(parameterN);
        }
        if (freeCupN == 0) {
            throw new SQLException("Configuration with key [n] can't has [zero] value! ");
        }

        // take configuration for min Order Total Sum for free delivery
        String parameterX = configurationService.getValue("x");
        double minOrderTotalForFreeDeliveryX;
        if (parameterX == null) {
            throw new SQLException("There is no default configuration or configuration " +
                    "stored in the database for the key: [x]");
        } else {
            minOrderTotalForFreeDeliveryX = Double.valueOf(parameterX);
        }
        // take configuration for delivery price
        String parameterM = configurationService.getValue("m");
        double deliveryPriceM;
        if (parameterM == null) {
            throw new SQLException("There is no default configuration or configuration " +
                    "stored in the database for the key: [m]");
        } else {
            deliveryPriceM = Double.valueOf(parameterM);
        }

        // calculate cost for Order items list
        int cupsNumberInOrder = 0;
        double costCoffee = 0;
        for (CoffeeOrderItem item : coffeeOrderItemList) {
            CoffeeType coffeeType = coffeeTypeDao.get(item.getCoffeeTypeId());
            if (coffeeType == null) {
                throw new SQLException("There is no in database CoffeeType with id: " + item.getId());
            }
            for (int i = 0; i < item.getQuantity(); i++) {
                cupsNumberInOrder++;
                if ( (cupsNumberInOrder % freeCupN) != 0 ) {
                    // every n cup is free
                    costCoffee += coffeeType.getPrice();
                }
            }
        }

        Cost cost = new Cost();
        cost.setCoffeeTotalCost(costCoffee);
        // if order costCoffee more then minOrderTotalForFreeDeliveryX - delivery free
        cost.setDeliveryCost(costCoffee > minOrderTotalForFreeDeliveryX ? 0.0 : deliveryPriceM);

        return cost;
    }
}
