package services.impl;

import dao.CoffeeOrderItemDao;
import entities.CoffeeOrderItem;
import org.apache.log4j.Logger;
import services.CoffeeOrderItemService;
import services.ServiceException;
import utils.SingletonBuilder;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public class CoffeeOrderItemServiceImpl extends AbstractService implements CoffeeOrderItemService {
    private static Logger log = Logger.getLogger(CoffeeOrderItemServiceImpl.class);

    private CoffeeOrderItemDao coffeeOrderItemDao = SingletonBuilder.getInstanceImpl(CoffeeOrderItemDao.class);

    private CoffeeOrderItemServiceImpl() {}

    /**
     * get all records of CoffeeOrderItem from DB for definite CoffeeOrder with id = orderId
     *
     * @param orderId determines id for CoffeeOrder
     * @return a list of all records of CoffeeOrderItem from the database
     * for definite CoffeeOrder with id = orderId or
     * empty list if there are no entries found.
     */
    @Override
    public List<CoffeeOrderItem> getAllForOrderId(Serializable orderId) {
        try {
            return coffeeOrderItemDao.getAllForOrderId(orderId);
        } catch (SQLException e) {
            String errorMessage = "Error getting all CoffeeOrderItems where CoffeeOrderItem.orderId: " +
                    orderId;
            log.error(errorMessage + e.getMessage());
            throw new ServiceException(errorMessage);
        }
    }
}
