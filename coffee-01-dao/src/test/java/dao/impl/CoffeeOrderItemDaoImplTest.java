package dao.impl;

import dao.CoffeeOrderDao;
import dao.CoffeeOrderItemDao;
import dao.CoffeeTypeDao;
import db.ConnectionManager;
import entities.CoffeeOrder;
import entities.CoffeeOrderItem;
import entities.CoffeeType;
import entities.enums.DisabledFlag;
import org.junit.Assert;
import org.junit.Test;
import utils.SingletonBuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class CoffeeOrderItemDaoImplTest extends Assert {
    private CoffeeOrderItemDao dao = SingletonBuilder.getInstanceImpl(CoffeeOrderItemDao.class);
    private CoffeeOrderDao daoCoffeeOrder = SingletonBuilder.getInstanceImpl(CoffeeOrderDao.class);
    private CoffeeTypeDao daoCoffeeType = SingletonBuilder.getInstanceImpl(CoffeeTypeDao.class);

    @Test
    public void crud()  throws SQLException {
        Connection con = ConnectionManager.getConnection();
        con.setAutoCommit(false);

        CoffeeOrderItem saved;
        CoffeeOrderItem getIt;
        CoffeeOrderItem updated;

        CoffeeOrderItem newOneForSave = new CoffeeOrderItem();
        newOneForSave.setQuantity(3);

        CoffeeType coffeeTypeForSave = new CoffeeType();
        coffeeTypeForSave.setTypeName("Very fragrant coffee");
        coffeeTypeForSave.setPrice(1.0);
        coffeeTypeForSave.setDisabled(DisabledFlag.Y);

        CoffeeOrder coffeeOrderForSave = new CoffeeOrder();
        Date currentDate = new java.util.Date();
        java.sql.Timestamp timestamp = new java.sql.Timestamp(currentDate.getTime());
        coffeeOrderForSave.setOrderDate(timestamp);
        coffeeOrderForSave.setCustomerName("Ivanov Ivan");
        coffeeOrderForSave.setDeliveryAddress("Sunny street, 12");
        coffeeOrderForSave.setCost(12.0);

        try {
            // check null save & update
            CoffeeOrderItem nullSave = dao.save(null);
            assertNull(nullSave);
            dao.update(nullSave);
            assertNull(nullSave);

            // save CoffeeType & CoffeeOrder
            coffeeTypeForSave = daoCoffeeType.save(coffeeTypeForSave);
            coffeeOrderForSave = daoCoffeeOrder.save(coffeeOrderForSave);

            // check save and get
            newOneForSave.setCoffeeTypeId(coffeeTypeForSave.getId());
            newOneForSave.setOrderId(coffeeOrderForSave.getId());
            saved = dao.save(newOneForSave);
            getIt = dao.get(saved.getId());

            assertNotNull(saved);
            assertNotNull(getIt);
            assertEquals(saved, getIt);

            // check update
            CoffeeOrderItem newOneForUpdate = dao.get(saved.getId());
            newOneForUpdate.setQuantity(5);
            dao.update(newOneForUpdate);
            updated = dao.get(newOneForUpdate.getId());

            assertNotEquals(saved, updated);
            assertNotNull(updated);
            assertEquals(newOneForUpdate, updated);
            assertEquals(saved.getId(), updated.getId());

            // check delete
            int delNumber = dao.delete(getIt.getId());
            assertEquals(delNumber, 1);
            getIt = dao.get(saved.getId());
            assertNull(getIt);

            delNumber = daoCoffeeType.delete(coffeeTypeForSave.getId());
            assertEquals(delNumber, 1);
            CoffeeType getCoffeeType = daoCoffeeType.get(coffeeTypeForSave.getId());
            assertNull(getCoffeeType);

            delNumber = daoCoffeeOrder.delete(coffeeOrderForSave.getId());
            assertEquals(delNumber, 1);
            CoffeeOrder getCoffeeOrder = daoCoffeeOrder.get(coffeeTypeForSave.getId());
            assertNull(getCoffeeOrder);

        } finally {
            con.rollback();
        }
    }

    @Test
    public void getAllForOrderId() throws SQLException {
        Connection con = ConnectionManager.getConnection();
        con.setAutoCommit(false);

        CoffeeOrderItem newOneForSave = new CoffeeOrderItem();
        newOneForSave.setQuantity(3);

        CoffeeType coffeeTypeForSave = new CoffeeType();
        coffeeTypeForSave.setTypeName("Very fragrant coffee");
        coffeeTypeForSave.setPrice(1.0);
        coffeeTypeForSave.setDisabled(DisabledFlag.Y);

        CoffeeOrder coffeeOrderForSave = new CoffeeOrder();
        Date currentDate = new java.util.Date();
        java.sql.Timestamp timestamp = new java.sql.Timestamp(currentDate.getTime());
        coffeeOrderForSave.setOrderDate(timestamp);
        coffeeOrderForSave.setCustomerName("Ivanov Ivan");
        coffeeOrderForSave.setDeliveryAddress("Sunny street, 12");
        coffeeOrderForSave.setCost(12.0);

        try {
            // save CoffeeType & CoffeeOrder
            coffeeTypeForSave = daoCoffeeType.save(coffeeTypeForSave);
            coffeeOrderForSave = daoCoffeeOrder.save(coffeeOrderForSave);

            // check getAllForOrderId
            List<CoffeeOrderItem> list1 = dao.getAllForOrderId(coffeeOrderForSave.getId());

            newOneForSave.setCoffeeTypeId(coffeeTypeForSave.getId());
            newOneForSave.setOrderId(coffeeOrderForSave.getId());
            newOneForSave = dao.save(newOneForSave);
            List<CoffeeOrderItem> list2 = dao.getAllForOrderId(coffeeOrderForSave.getId());

            assertEquals(list2.size() - list1.size() , 1);

            // delete saved entities
            int delNumber = dao.delete(newOneForSave.getId());
            assertEquals(delNumber, 1);
            CoffeeOrderItem getCoffeeOrderItem = dao.get(newOneForSave.getId());
            assertNull(getCoffeeOrderItem);

            delNumber = daoCoffeeType.delete(coffeeTypeForSave.getId());
            assertEquals(delNumber, 1);
            CoffeeType getCoffeeType = daoCoffeeType.get(coffeeTypeForSave.getId());
            assertNull(getCoffeeType);

            delNumber = daoCoffeeOrder.delete(coffeeOrderForSave.getId());
            assertEquals(delNumber, 1);
            CoffeeOrder getCoffeeOrder = daoCoffeeOrder.get(coffeeTypeForSave.getId());
            assertNull(getCoffeeOrder);
        } finally {
            con.rollback();
        }
    }
}
