package dao.impl;

import dao.ConfigurationDao;
import db.ConnectionManager;
import entities.Configuration;
import org.junit.Assert;
import org.junit.Test;
import utils.SingletonBuilder;

import java.sql.Connection;
import java.sql.SQLException;

public class ConfigurationDaoImplTest extends Assert {
    private ConfigurationDao dao = SingletonBuilder.getInstanceImpl(ConfigurationDao.class);

    @Test
    public void crud()  throws SQLException {
        Connection con = ConnectionManager.getConnection();
        con.setAutoCommit(false);

        Configuration saved;
        Configuration getIt;
        Configuration updated;

        Configuration newOneForSave = new Configuration();
        newOneForSave.setId("x");
        newOneForSave.setValue("3");

        Configuration newOneForUpdate = new Configuration();
        newOneForUpdate.setValue("1");

        try {
            // check null save
            Configuration nullSave = dao.save(null);
            assertNull(nullSave);
            dao.update(nullSave);
            assertNull(nullSave);

            nullSave = new Configuration();
            assertNotNull(nullSave);
            assertNull(nullSave.getId());
            nullSave = dao.save(null);
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
}
