package services.impl;

import dao.CoffeeTypeDao;
import entities.CoffeeType;
import entities.enums.DisabledFlag;
import org.apache.log4j.Logger;
import services.CoffeeTypeService;
import services.ServiceException;
import utils.SingletonBuilder;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public class CoffeeTypeServiceImpl extends AbstractService implements CoffeeTypeService {
    private static Logger log = Logger.getLogger(CoffeeTypeServiceImpl.class);

    private CoffeeTypeDao coffeeTypeDao = SingletonBuilder.getInstanceImpl(CoffeeTypeDao.class);

    private CoffeeTypeServiceImpl() {}

    /**
     * returns an CoffeeType record with an id = coffeeTypeId from the database
     *
     * @param coffeeTypeId determine id of CoffeeType record in database
     * @return CoffeeType record from the database with id = coffeeTypeId, or
     * null if such an entity was not found
     */
    @Override
    public CoffeeType get(Serializable coffeeTypeId) {
        try {
            return coffeeTypeDao.get(coffeeTypeId);
        } catch (SQLException e) {
            String errorMessage = "Error getting CoffeeType by id: " + coffeeTypeId;
            log.error(errorMessage + e.getMessage());
            throw new ServiceException(errorMessage);
        }
    }

    /**
     * Saves the coffeeType in the database if @param coffeeType != null
     *
     * @param coffeeType determine entity with type <CoffeeType>
     * @return saved entity with not null id or
     *         null if coffeeType = null
     */
    @Override
    public CoffeeType add(CoffeeType coffeeType) {
        if (coffeeType == null) {
            return null;
        }
        try {
            this.startTransaction();
            CoffeeType coffeeTypeSave = coffeeTypeDao.save(coffeeType);
            this.commit();
            this.stopTransaction();
            return coffeeTypeSave;
        } catch (SQLException e) {
            rollback();
            String errorMessage = "Error saving CoffeeType: " + coffeeType;
            log.error(errorMessage + e.getMessage());
            throw new ServiceException(errorMessage);
        }
    }

    /**
     * update an CoffeeType entity with an id = coffeeType.id in the database
     * if @param coffeeType != null
     *
     * @param coffeeType determine a new entity to be updated
     *                   in the database with id = coffeeType.id
     */
    @Override
    public void update(CoffeeType coffeeType) {
        if (coffeeType == null) {
            return;
        }
        try {
            this.startTransaction();
            coffeeTypeDao.update(coffeeType);
            this.commit();
            this.stopTransaction();
        } catch (SQLException e) {
            rollback();
            String errorMessage = "Error updating CoffeeType: " + coffeeType;
            log.error(errorMessage + e.getMessage());
            throw new ServiceException(errorMessage);
        }
    }

    /**
     * removes from the database an CoffeeType entity with an id = coffeeTypeId
     *
     * @param coffeeTypeId determine id of entity in database
     * @return returns the number of deleted rows from the database
     */
    @Override
    public int delete(Serializable coffeeTypeId) {
        try {
            this.startTransaction();
            int deletedRecords = coffeeTypeDao.delete(coffeeTypeId);
            this.commit();
            this.stopTransaction();

            return deletedRecords;
        } catch (SQLException e) {
            rollback();
            String errorMessage = "Error deleting from DB CoffeeType with id: " + coffeeTypeId;
            log.error(errorMessage + e.getMessage());
            throw new ServiceException(errorMessage);
        }
    }

    /**
     * get all records of CoffeeTypes from DB
     *
     * @return a list of all records of CoffeeTypes from the database
     *         or empty list if there are no entries
     */
    @Override
    public List<CoffeeType> getAll() {
        try {
            return coffeeTypeDao.getAll();
        } catch (SQLException e) {
            String errorMessage = "Error getting all CoffeeType from database.";
            log.error(errorMessage + e.getMessage());
            throw new ServiceException(errorMessage);
        }
    }

    /**
     * get all records of CoffeeTypes from DB where CoffeeType.disabled = disabledFlag
     *
     * @param disabledFlag determines whether ("N") or not ("Y") to show on the UI given CoffeeType
     * @return a list of all records of CoffeeTypes from the database
     *         where CoffeeType.disabled = disabledFlag or
     *         empty list if there are no entries
     */
    @Override
    public List<CoffeeType> getAllForDisabledFlag(DisabledFlag disabledFlag) {
        try {
            return coffeeTypeDao.getAllForDisabledFlag(disabledFlag);
        } catch (SQLException e) {
            String errorMessage = "Error getting all CoffeeType where CoffeeType.disabled: " + disabledFlag;
            log.error(errorMessage + e.getMessage());
            throw new ServiceException(errorMessage);
        }
    }
}
