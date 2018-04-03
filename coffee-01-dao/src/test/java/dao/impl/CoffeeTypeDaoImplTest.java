package dao.impl;

import dao.CoffeeTypeDao;
import db.ConnectionManager;
import entities.CoffeeType;
import entities.enums.DisabledFlag;
import org.junit.Assert;
import org.junit.Test;
import utils.SingletonBuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CoffeeTypeDaoImplTest extends Assert {
    private CoffeeTypeDao dao = SingletonBuilder.getInstanceImpl(CoffeeTypeDao.class);

    @Test
    public void crud()  throws SQLException {
        Connection con = ConnectionManager.getConnection();
        con.setAutoCommit(false);
        CoffeeType saved;
        CoffeeType getIt;
        CoffeeType updated;

        CoffeeType newOneForSave = new CoffeeType();
        newOneForSave.setTypeName("Very fragrant coffee");
        newOneForSave.setPrice(1.0);
        newOneForSave.setDisabled(DisabledFlag.Y);

        CoffeeType newOneForUpdate = new CoffeeType();
        newOneForUpdate.setTypeName("Black coffee with cream");
        newOneForUpdate.setPrice(3.0);
        newOneForUpdate.setDisabled(DisabledFlag.N);

        try {
            // check null save & update
            CoffeeType nullSave = dao.save(null);
            assertNull(nullSave);
            dao.update(nullSave);
            assertNull(nullSave);

            // check save and get
            saved = dao.save(newOneForSave);
            getIt = dao.get(saved.getId());

            assertNotNull(saved);
            assertNotNull(getIt);
            assertEquals(saved, getIt);

            // check update
            newOneForUpdate.setId(saved.getId());
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

    @Test
    public void getAll() throws SQLException {
        Connection con = ConnectionManager.getConnection();
        con.setAutoCommit(false);

        CoffeeType newOneForSave = new CoffeeType();
        newOneForSave.setTypeName("Very fragrant coffee");
        newOneForSave.setPrice(1.0);
        newOneForSave.setDisabled(DisabledFlag.Y);
        try {
            List<CoffeeType> list1 = dao.getAll();

            dao.save(newOneForSave);
            List<CoffeeType> list2 = dao.getAll();

            assertEquals(list2.size() - list1.size() , 1);

            for (CoffeeType element : list2) {
                dao.delete(element.getId());
            }
            List<CoffeeType> listAfterDeleteAll = dao.getAll();
            assertEquals(listAfterDeleteAll.size(),0);
        } finally {
            con.rollback();
        }
    }

    @Test
    public void getAllForDisabledFlag() throws SQLException {
        Connection con = ConnectionManager.getConnection();
        con.setAutoCommit(false);

        CoffeeType newOneForSave = new CoffeeType();
        newOneForSave.setTypeName("Very fragrant coffee");
        newOneForSave.setPrice(1.0);
        newOneForSave.setDisabled(DisabledFlag.Y);
        try {
            List<CoffeeType> list1 = dao.getAllForDisabledFlag(DisabledFlag.Y);

            dao.save(newOneForSave);
            newOneForSave.setDisabled(DisabledFlag.N);
            newOneForSave.setTypeName("Black coffee with cream");
            dao.save(newOneForSave);
            List<CoffeeType> list2 = dao.getAllForDisabledFlag(DisabledFlag.Y);

            assertEquals(list2.size() - list1.size() , 1);

            for (CoffeeType element : dao.getAll()) {
                dao.delete(element.getId());
            }
            List<CoffeeType> listAfterDeleteAll = dao.getAll();
            assertEquals(listAfterDeleteAll.size(),0);
        } finally {
            con.rollback();
        }
    }
}
