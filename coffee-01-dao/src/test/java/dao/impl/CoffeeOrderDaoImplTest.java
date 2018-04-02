package dao.impl;

import dao.CoffeeOrderDao;
import db.ConnectionManager;
import entities.CoffeeOrder;
import org.junit.Assert;
import org.junit.Test;
import utils.SingletonBuilder;

import java.sql.Connection;

import java.sql.SQLException;
import java.util.Date;

public class CoffeeOrderDaoImplTest extends Assert {
    private CoffeeOrderDao dao = SingletonBuilder.getInstanceImpl(CoffeeOrderDao.class);

    @Test
    public void crud()  throws SQLException {
        Connection con = ConnectionManager.getConnection();
        con.setAutoCommit(false);

        CoffeeOrder saved;
        CoffeeOrder getIt;
        CoffeeOrder updated;

        CoffeeOrder newOneForSave = new CoffeeOrder();
        Date currentDate = new java.util.Date();
        java.sql.Timestamp timestamp = new java.sql.Timestamp(currentDate.getTime());
        newOneForSave.setOrderDate(timestamp);
        newOneForSave.setCustomerName("Ivanov Ivan");
        newOneForSave.setDeliveryAddress("Sunny street, 12");
        newOneForSave.setCost(12.0);

        CoffeeOrder newOneForUpdate = new CoffeeOrder();
        newOneForUpdate.setOrderDate(timestamp);
        newOneForUpdate.setCustomerName("Pavlov Pavel");
        newOneForUpdate.setDeliveryAddress("Wide street, 10");
        newOneForUpdate.setCost(10.0);

        try {
            // check null save & update
            CoffeeOrder nullSave = dao.save(null);
            assertNull(nullSave);
            dao.update(nullSave);
            assertNull(nullSave);

            // check save and get
            saved = dao.save(newOneForSave);
            getIt = dao.get(saved.getId());

            assertNotNull(saved);
            assertNotNull(getIt);
            assertEquals(saved.getId(), getIt.getId());
            assertEquals(saved.getCustomerName(), getIt.getCustomerName());
            assertEquals(saved.getDeliveryAddress(), getIt.getDeliveryAddress());
            assertEquals(saved.getCost(), getIt.getCost(), 0.00001);

            // check update
            newOneForUpdate.setId(saved.getId());
            newOneForUpdate.setOrderDate(getIt.getOrderDate());
            dao.update(newOneForUpdate);
            updated = dao.get(newOneForUpdate.getId());

            assertNotEquals(saved, updated);
            assertNotNull(updated);
            assertEquals(newOneForUpdate,updated);
            assertEquals(saved.getId(),updated.getId());

            // check delete
            int delNumber = dao.delete(getIt.getId());
            assertEquals(delNumber, 1);
            getIt = dao.get(saved.getId());
            assertNull(getIt);

        } finally {
            con.rollback();
        }
    }
}
