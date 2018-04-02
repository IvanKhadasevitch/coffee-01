package dao.impl;

import dao.CoffeeOrderDao;
import entities.CoffeeOrder;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CoffeeOrderDaoImpl extends AbstractDao implements CoffeeOrderDao {
    private static Logger log = Logger.getLogger(CoffeeOrderDaoImpl.class);

    private static final String SAVE_COFFEE_ORDER_SQL =
            "INSERT INTO CoffeeOrder (order_date, `name`, delivery_address, cost) VALUES (?,?,?,?)";
    private static final String GET_COFFEE_ORDER_BY_ID_SQL = "SELECT * FROM CoffeeOrder WHERE id=?";
    private static final String UPDATE_COFFEE_ORDER_BY_ID_SQL =
            "UPDATE CoffeeOrder SET order_date=?, `name`=?, delivery_address=?, cost=? WHERE id=?";
    private static final String DELETE_COFFEE_ORDER_BY_ID_SQL = "DELETE FROM CoffeeOrder WHERE id=?";

    private PreparedStatement psSave = null;
    private PreparedStatement psGet = null;
    private PreparedStatement psUpdate = null;
    private PreparedStatement psDelete = null;

    private CoffeeOrderDaoImpl() {}

    /**
     * Saves the entity type <CoffeeOrder> in the database
     *
     * @param coffeeOrder determine entity with type <CoffeeOrder>
     * @return saved entity with not null id
     * @throws SQLException if can't save entity
     */
    @Override
    public CoffeeOrder save(CoffeeOrder coffeeOrder) throws SQLException {
        if (coffeeOrder == null) {
            return null;
        }

        if (psSave == null) {
            psSave = psSave = prepareStatement(SAVE_COFFEE_ORDER_SQL, Statement.RETURN_GENERATED_KEYS);
        }
        psSave.setTimestamp(1, coffeeOrder.getOrderDate());
        psSave.setString(2, coffeeOrder.getCustomerName());
        psSave.setString(3, coffeeOrder.getDeliveryAddress());
        psSave.setDouble(4, coffeeOrder.getCost());
        psSave.executeUpdate();

        try (ResultSet rs = psSave.getGeneratedKeys()) {
            if (rs.next()) {
                coffeeOrder.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            String errorMessage = "Can't execute SQL: " + psSave + e.getMessage();
            log.error(errorMessage);
            throw new SQLException(errorMessage);
        }
        return coffeeOrder;
    }

    /**
     * returns an entity with an id from the database
     *
     * @param id determine id of entity in database
     * @return entity with type <CoffeeOrder> from the database,
     *         or null if such an entity was not found
     * @throws SQLException if there is an error connecting to the database
     */
    @Override
    public CoffeeOrder get(Serializable id) throws SQLException {
        if (psGet == null) {
            psGet = prepareStatement(GET_COFFEE_ORDER_BY_ID_SQL);
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
     * update an entity with an id = coffeeOrder.id in the database
     *
     * @param coffeeOrder determine a new entity to be updated
     *                    in the database with id = coffeeOrder.id
     * @throws SQLException if there is an error updating entity in the database
     */
    @Override
    public void update(CoffeeOrder coffeeOrder) throws SQLException {
        if (coffeeOrder == null) {
            return;
        }
        try {
            if (psUpdate == null) {
                psUpdate = prepareStatement(UPDATE_COFFEE_ORDER_BY_ID_SQL);
            }
            psUpdate.setInt(5, coffeeOrder.getId());
            psUpdate.setTimestamp(1, coffeeOrder.getOrderDate());
            psUpdate.setString(2, coffeeOrder.getCustomerName());
            psUpdate.setString(3, coffeeOrder.getDeliveryAddress());
            psUpdate.setDouble(4, coffeeOrder.getCost());
            psUpdate.executeUpdate();
        } catch (SQLException e) {
            String errorMessage = "Can't execute SQL: " + psUpdate + e.getMessage();
            log.error(errorMessage);
            throw new SQLException(errorMessage);
        }
    }

    /**
     * removes from the database an entity with type <CoffeeOrder> and id
     *
     * @param id determine id of entity in database
     * @return returns the number of deleted rows from the database
     * @throws SQLException if there is an error deleting entity from the database
     */
    @Override
    public int delete(Serializable id) throws SQLException {
        try {
            if (psDelete == null) {
                psDelete = prepareStatement(DELETE_COFFEE_ORDER_BY_ID_SQL);
            }
            psDelete.setInt(1, (int) id);
            return psDelete.executeUpdate();
        } catch (SQLException e) {
            String errorMessage = "Can't execute SQL: " + psDelete + e.getMessage();
            log.error(errorMessage);
            throw new SQLException(errorMessage);
        }
    }

    private CoffeeOrder populateEntity(ResultSet rs) throws SQLException {
        CoffeeOrder entity = new CoffeeOrder();
        entity.setId(rs.getInt(1));
        entity.setOrderDate(rs.getTimestamp(2) );
        entity.setCustomerName(rs.getString(3) );
        entity.setDeliveryAddress(rs.getString(4));
        entity.setCost(rs.getDouble(5));

        return entity;
    }
}
