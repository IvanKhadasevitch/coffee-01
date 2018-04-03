package services;

import entities.CoffeeType;
import entities.enums.DisabledFlag;

import java.io.Serializable;
import java.util.List;

public interface CoffeeTypeService {
    /**
     * returns an CoffeeType record with an id = coffeeTypeId from the database
     *
     * @param coffeeTypeId determine id of CoffeeType record in database
     * @return CoffeeType record from the database with id = coffeeTypeId, or
     *         null if such an entity was not found
     */
    CoffeeType get(Serializable coffeeTypeId);

    /**
     * Saves the coffeeType in the database if @param coffeeType != null
     *
     * @param coffeeType determine entity with type <CoffeeType>
     * @return saved entity with not null id or
     *         null if coffeeType = null
     */
    CoffeeType add(CoffeeType coffeeType);

    /**
     * update an CoffeeType entity with an id = coffeeType.id in the database
     * if @param coffeeType != null
     *
     * @param coffeeType determine a new entity to be updated
     *                   in the database with id = coffeeType.id
     */
    void update(CoffeeType coffeeType);

    /**
     * removes from the database an CoffeeType entity with an id = coffeeTypeId
     *
     * @param coffeeTypeId determine id of entity in database
     * @return returns the number of deleted rows from the database
     */
    int delete(Serializable coffeeTypeId);

    /**
     * get all records of CoffeeTypes from DB
     *
     * @return a list of all records of CoffeeTypes from the database
     *         or empty list if there are no entries
     */
    List<CoffeeType> getAll();

    /**
     * get all records of CoffeeTypes from DB where CoffeeType.disabled = disabledFlag
     *
     * @param disabledFlag determines whether ("N") or not ("Y") to show on the UI given CoffeeType
     * @return a list of all records of CoffeeTypes from the database
     *         where CoffeeType.disabled = disabledFlag or
     *         empty list if there are no entries
     */
    List<CoffeeType> getAllForDisabledFlag(DisabledFlag disabledFlag);
}
