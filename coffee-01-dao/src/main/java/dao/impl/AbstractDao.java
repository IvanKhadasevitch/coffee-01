package dao.impl;

import db.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbstractDao{
    protected PreparedStatement prepareStatement(String query) throws SQLException {
        return ConnectionManager.getConnection().prepareStatement(query);
    }

    protected PreparedStatement prepareStatement(String query, int flag) throws SQLException {
        return ConnectionManager.getConnection().prepareStatement(query, flag);
    }

}
