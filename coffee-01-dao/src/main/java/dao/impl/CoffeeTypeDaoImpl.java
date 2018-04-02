package dao.impl;

import dao.CoffeeTypeDao;
import entities.CoffeeType;
import entities.enums.DisabledFlag;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CoffeeTypeDaoImpl extends AbstractDao implements CoffeeTypeDao {
    private static Logger log = Logger.getLogger(CoffeeTypeDaoImpl.class);

    private static final String SAVE_COFFEE_TYPE_SQL =
            "INSERT INTO CoffeeType (type_name, price, disabled) VALUES (?,?,?)";
    private static final String GET_COFFEE_TYPE_BY_ID_SQL = "SELECT * FROM CoffeeType WHERE id=?";
    private static final String GET_ALL_COFFEE_TYPE_SQL = "SELECT * FROM CoffeeType";
    private static final String GET_ALL_COFFEE_TYPE_FOR_DISABLED_FLAG_SQL =
            "SELECT * FROM CoffeeType WHERE disabled=?";
    private static final String UPDATE_COFFEE_TYPE_BY_ID_SQL =
            "UPDATE CoffeeType SET type_name=?, price=?, disabled=? WHERE id=?";
    private static final String DELETE_COFFEE_TYPE_BY_ID_SQL = "DELETE FROM CoffeeType WHERE id=?";

    private PreparedStatement psSave = null;
    private PreparedStatement psGet = null;
    private PreparedStatement psGetAll = null;
    private PreparedStatement psGetAllForDisabledFlag = null;
    private PreparedStatement psUpdate = null;
    private PreparedStatement psDelete = null;

    private CoffeeTypeDaoImpl() {}

    /**
     * Saves the entity type <CoffeeType> in the database
     *
     * @param coffeeType determine entity with type <CoffeeType>
     * @return saved entity with not null id
     * @throws SQLException if can't save entity
     */
    @Override
    public CoffeeType save(CoffeeType coffeeType) throws SQLException {
        if (coffeeType == null) {
            return null;
        }

        if (psSave == null) {
            psSave = psSave = prepareStatement(SAVE_COFFEE_TYPE_SQL, Statement.RETURN_GENERATED_KEYS);
        }
        psSave.setString(1, coffeeType.getTypeName());
        psSave.setDouble(2, coffeeType.getPrice());
        psSave.setString(3, String.valueOf(coffeeType.getDisabled()));
        psSave.executeUpdate();

        try (ResultSet rs = psSave.getGeneratedKeys()) {
            if (rs.next()) {
                coffeeType.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            String errorMessage = "Can't execute SQL: " + psSave + e.getMessage();
            log.error(errorMessage);
            throw new SQLException(errorMessage);
        }
        return coffeeType;
    }

    /**
     * returns an entity with an id from the database
     *
     * @param id determine id of entity in database
     * @return entity with type <CoffeeType> from the database,
     *         or null if such an entity was not found
     * @throws SQLException if there is an error connecting to the database
     */
    @Override
    public CoffeeType get(Serializable id) throws SQLException {
        if (psGet == null) {
            psGet = prepareStatement(GET_COFFEE_TYPE_BY_ID_SQL);
        }
        psGet.setInt(1, (int) id);
        psGet.executeQuery();

        try (ResultSet rs = psGet.getResultSet()) {
            if (rs.next()) {
                return populateEntity(rs);
            }
        } catch (SQLException e) {
            String errorMessage = "Can't execute SQL: " + psGet + e.getMessage();
            log.error(errorMessage);
            throw new SQLException(errorMessage);
        }
        return null;
    }

    /**
     * update an entity with an id = coffeeType.id in the database
     *
     * @param coffeeType determine a new entity to be updated
     *                   in the database with id = coffeeType.id
     * @throws SQLException if there is an error updating entity in the database
     */
    @Override
    public void update(CoffeeType coffeeType) throws SQLException {
        if (coffeeType == null) {
            return;
        }
        try {
            if (psUpdate == null) {
                psUpdate = prepareStatement(UPDATE_COFFEE_TYPE_BY_ID_SQL);
            }
            psUpdate.setInt(4, coffeeType.getId());
            psUpdate.setString(1, coffeeType.getTypeName());
            psUpdate.setDouble(2, coffeeType.getPrice());
            psUpdate.setString(3, String.valueOf(coffeeType.getDisabled()));
            psUpdate.executeUpdate();
        } catch (SQLException e) {
            String errorMessage = "Can't execute SQL: " + psUpdate + e.getMessage();
            log.error(errorMessage);
            throw new SQLException(errorMessage);
        }
    }

    /**
     * removes from the database an entity with type <CoffeeType> and id
     *
     * @param id determine id of entity in database
     * @return returns the number of deleted rows from the database
     * @throws SQLException if there is an error deleting entity from the database
     */
    @Override
    public int delete(Serializable id) throws SQLException {
        try {
            if (psDelete == null) {
                psDelete = prepareStatement(DELETE_COFFEE_TYPE_BY_ID_SQL);
            }
            psDelete.setInt(1, (int) id);
            return psDelete.executeUpdate();
        } catch (SQLException e) {
            String errorMessage = "Can't execute SQL: " + psDelete + e.getMessage();
            log.error(errorMessage);
            throw new SQLException(errorMessage);
        }
    }

    /**
     * get all CoffeeTypes from DB
     *
     * @return a list of all records of CoffeeTypes from the database
     * or empty list if there are no entries
     * @throws SQLException if there is an error connecting to the database
     */
    @Override
    public List<CoffeeType> getAll() throws SQLException {
        List<CoffeeType> list = new ArrayList<>();
        try {
            if (psGetAll == null) {
                psGetAll = prepareStatement(GET_ALL_COFFEE_TYPE_SQL);
            }
            psGetAll.execute();
            try (ResultSet rs = psGetAll.getResultSet()) {
                while (rs.next()) {
                    list.add(populateEntity(rs));
                }
            } catch (SQLException e) {
                String errorMessage = "Can't execute SQL: " + psGetAll + e.getMessage();
                log.error(errorMessage);
                throw new SQLException(errorMessage);
            }
        } catch (SQLException e) {
            String errorMessage = "Can't execute SQL: " + psGetAll + e.getMessage();
            log.error(errorMessage);
            throw new SQLException(errorMessage);
        }

        return list;
    }

    /**
     * get all records of CoffeeTypes from DB where CoffeeType.disabled = disabledFlag
     *
     * @param disabledFlag determines whether ("Y") or not ("N") to show on the UI given CoffeeType
     * @return a list of all records of CoffeeTypes from the database
     * where CoffeeType.disabled = disabledFlag or
     * empty list if there are no entries
     * @throws SQLException if there is an error connecting to the database
     */
    @Override
    public List<CoffeeType> getAllForDisabledFlag(DisabledFlag disabledFlag) throws SQLException {
        List<CoffeeType> list = new ArrayList<>();
        try {
            if (psGetAllForDisabledFlag == null) {
                psGetAllForDisabledFlag = prepareStatement(GET_ALL_COFFEE_TYPE_FOR_DISABLED_FLAG_SQL);
            }
            psGetAllForDisabledFlag.setString(1, String.valueOf(disabledFlag));
            psGetAllForDisabledFlag.execute();
            try (ResultSet rs = psGetAllForDisabledFlag.getResultSet()) {
                while (rs.next()) {
                    list.add(populateEntity(rs));
                }
            } catch (SQLException e) {
                String errorMessage = "Can't execute SQL: " + psGetAllForDisabledFlag +
                        e.getMessage();
                log.error(errorMessage);
                throw new SQLException(errorMessage);
            }
        } catch (SQLException e) {
            String errorMessage = "Can't execute SQL: " + psGetAllForDisabledFlag +
                    e.getMessage();
            log.error(errorMessage);
            throw new SQLException(errorMessage);
        }

        return list;
    }

    private CoffeeType populateEntity(ResultSet rs) throws SQLException {
        CoffeeType entity = new CoffeeType();
        entity.setId(rs.getInt(1));
        entity.setTypeName(rs.getString(2));
        entity.setPrice(rs.getDouble(3));
        entity.setDisabled(DisabledFlag.valueOf(rs.getString(4)));

        return entity;
    }
}
