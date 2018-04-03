package dao;

import entities.CoffeeType;
import entities.enums.DisabledFlag;

import java.sql.SQLException;
import java.util.List;

public interface CoffeeTypeDao extends DAO<CoffeeType> {
    /**
     *
     * get all records of CoffeeTypes from DB
     *
     * @return a list of all records of CoffeeTypes from the database
     *         or empty list if there are no entries
     * @throws SQLException if there is an error connecting to the database
     */
    List<CoffeeType> getAll() throws SQLException;

    /**
     *
     * get all records of CoffeeTypes from DB where CoffeeType.disabled = disabledFlag
     *
     * @param disabledFlag determines whether ("Y") or not ("N") to show on the UI given CoffeeType
     * @return a list of all records of CoffeeTypes from the database
     *         where CoffeeType.disabled = disabledFlag or
     *         empty list if there are no entries
     * @throws SQLException if there is an error connecting to the database
     */
    List<CoffeeType> getAllForDisabledFlag(DisabledFlag disabledFlag) throws SQLException;
}
