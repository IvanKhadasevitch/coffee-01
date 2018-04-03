package services.impl;

import db.ConnectionManager;
import services.ServiceException;

import java.sql.Connection;
import java.sql.SQLException;

public class AbstractService {
    public void startTransaction() throws SQLException {
        ConnectionManager.getConnection().setAutoCommit(false);
    }

    public void stopTransaction() throws SQLException {
        ConnectionManager.getConnection().setAutoCommit(true);
    }

    public void commit() throws SQLException {
        ConnectionManager.getConnection().commit();
    }

    public Connection getConnection() {
        return ConnectionManager.getConnection();
    }

    public void rollback() {
        try {
            getConnection().rollback();
        } catch (SQLException e) {
            throw new ServiceException("rollback error");
        }
    }
}
