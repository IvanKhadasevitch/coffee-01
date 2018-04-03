package services;

import entities.CoffeeOrderItem;

import java.io.Serializable;
import java.util.List;

public interface CoffeeOrderItemService {
    /**
     * get all records of CoffeeOrderItem from DB for definite CoffeeOrder with id = orderId
     *
     * @param orderId determines id for CoffeeOrder
     * @return a list of all records of CoffeeOrderItem from the database
     *         for definite CoffeeOrder with id = orderId or
     *         empty list if there are no entries found.
     */
    List<CoffeeOrderItem> getAllForOrderId(Serializable orderId);
}
