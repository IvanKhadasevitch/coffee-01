package dao;

import entities.CoffeeOrderItem;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public interface CoffeeOrderItemDao extends DAO<CoffeeOrderItem> {
    /**
     * get all records of CoffeeOrderItem from DB for definite CoffeeOrder with id = orderId
     *
     * @param orderId determines id for CoffeeOrder
     * @return a list of all records of CoffeeOrderItem from the database
     *         for definite CoffeeOrder with id = orderId or
     *         empty list if there are no entries found.
     * @throws SQLException if there is an error connecting to the database
     */
    List<CoffeeOrderItem> getAllForOrderId(Serializable orderId) throws SQLException;
}

