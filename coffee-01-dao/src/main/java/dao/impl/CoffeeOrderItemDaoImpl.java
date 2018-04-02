package dao.impl;

import dao.CoffeeOrderItemDao;
import entities.CoffeeOrderItem;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CoffeeOrderItemDaoImpl extends AbstractDao implements CoffeeOrderItemDao {
    private static Logger log = Logger.getLogger(CoffeeOrderItemDaoImpl.class);

    private static final String SAVE_COFFEE_ORDER_ITEM_SQL =
            "INSERT INTO CoffeeOrderItem (type_id, order_id, quantity) VALUES (?,?,?)";
    private static final String GET_COFFEE_ORDER_ITEM_BY_ID_SQL =
            "SELECT * FROM CoffeeOrderItem WHERE id=?";
    private static final String GET_ALL_COFFEE_ORDER_ITEM_FOR_ORDER_ID_SQL =
            "SELECT * FROM CoffeeOrderItem WHERE order_id=?";
    private static final String UPDATE_COFFEE_ORDER_ITEM_BY_ID_SQL =
            "UPDATE CoffeeOrderItem SET type_id=?, order_id=?, quantity=? WHERE id=?";
    private static final String DELETE_COFFEE_ORDER_ITEM_BY_ID_SQL =
            "DELETE FROM CoffeeOrderItem WHERE id=?";

    private PreparedStatement psSave = null;
    private PreparedStatement psGet = null;
    private PreparedStatement psGetAllForOrderId = null;
    private PreparedStatement psUpdate = null;
    private PreparedStatement psDelete = null;

    private CoffeeOrderItemDaoImpl() {}

    /**
     * Saves the entity type <CoffeeOrderItem> in the database
     *
     * @param coffeeOrderItem determine entity with type <CoffeeOrderItem>
     * @return saved entity with not null id
     * @throws SQLException if can't save entity
     */
    @Override
    public CoffeeOrderItem save(CoffeeOrderItem coffeeOrderItem) throws SQLException {
        if (coffeeOrderItem == null) {
            return null;
        }

        if (psSave == null) {
            psSave = psSave = prepareStatement(SAVE_COFFEE_ORDER_ITEM_SQL, Statement.RETURN_GENERATED_KEYS);
        }
        psSave.setInt(1, coffeeOrderItem.getCoffeeTypeId());
        psSave.setInt(2, coffeeOrderItem.getOrderId());
        psSave.setInt(3, coffeeOrderItem.getQuantity());
        psSave.executeUpdate();

        try (ResultSet rs = psSave.getGeneratedKeys()) {
            if (rs.next()) {
                coffeeOrderItem.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            String errorMessage = "Can't execute SQL: " + psSave + e.getMessage();
            log.error(errorMessage);
            throw new SQLException(errorMessage);
        }
        return coffeeOrderItem;
    }

    /**
     * returns an entity with an id from the database
     *
     * @param id determine id of entity in database
     * @return entity with type <CoffeeOrderItem> from the database,
     *         or null if such an entity was not found
     * @throws SQLException if there is an error connecting to the database
     */
    @Override
    public CoffeeOrderItem get(Serializable id) throws SQLException {
        if (psGet == null) {
            psGet = prepareStatement(GET_COFFEE_ORDER_ITEM_BY_ID_SQL);
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
     * update an entity with an id = coffeeOrderItem.id in the database
     *
     * @param coffeeOrderItem determine a new entity to be updated
     *                        in the database with id = coffeeOrderItem.id
     * @throws SQLException if there is an error updating entity in the database
     */
    @Override
    public void update(CoffeeOrderItem coffeeOrderItem) throws SQLException {
        if (coffeeOrderItem == null) {
            return;
        }
        try {
            if (psUpdate == null) {
                psUpdate = prepareStatement(UPDATE_COFFEE_ORDER_ITEM_BY_ID_SQL);
            }
            psUpdate.setInt(4, coffeeOrderItem.getId());
            psUpdate.setInt(1, coffeeOrderItem.getCoffeeTypeId());
            psUpdate.setInt(2, coffeeOrderItem.getOrderId());
            psUpdate.setInt(3, coffeeOrderItem.getQuantity());
            psUpdate.executeUpdate();
        } catch (SQLException e) {
            String errorMessage = "Can't execute SQL: " + psUpdate + e.getMessage();
            log.error(errorMessage);
            throw new SQLException(errorMessage);
        }
    }

    /**
     * removes from the database an entity with type <CoffeeOrderItem> and id
     *
     * @param id determine id of entity in database
     * @return returns the number of deleted rows from the database
     * @throws SQLException if there is an error deleting entity from the database
     */
    @Override
    public int delete(Serializable id) throws SQLException {
        try {
            if (psDelete == null) {
                psDelete = prepareStatement(DELETE_COFFEE_ORDER_ITEM_BY_ID_SQL);
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
     * get all records of CoffeeOrderItem from DB for definite CoffeeOrder with id = orderId
     *
     * @param orderId determines id for CoffeeOrder
     * @return a list of all records of CoffeeOrderItem from the database
     *         for definite CoffeeOrder with id = orderId or
     *         empty list if there are no entries
     * @throws SQLException if there is an error connecting to the database
     */
    @Override
    public List<CoffeeOrderItem> getAllForOrderId(Serializable orderId) throws SQLException {
        List<CoffeeOrderItem> list = new ArrayList<>();
        try {
            if (psGetAllForOrderId == null) {
                psGetAllForOrderId = prepareStatement(GET_ALL_COFFEE_ORDER_ITEM_FOR_ORDER_ID_SQL);
            }
            psGetAllForOrderId.setInt(1, (int) orderId);
            psGetAllForOrderId.execute();
            try (ResultSet rs = psGetAllForOrderId.getResultSet()) {
                while (rs.next()) {
                    list.add(populateEntity(rs));
                }
            } catch (SQLException e) {
                String errorMessage = "Can't execute SQL: " + psGetAllForOrderId +
                        e.getMessage();
                log.error(errorMessage);
                throw new SQLException(errorMessage);
            }
        } catch (SQLException e) {
            String errorMessage = "Can't execute SQL: " + psGetAllForOrderId +
                    e.getMessage();
            log.error(errorMessage);
            throw new SQLException(errorMessage);
        }

        return list;
    }

    private CoffeeOrderItem populateEntity(ResultSet rs) throws SQLException {
        CoffeeOrderItem entity = new CoffeeOrderItem();
        entity.setId(rs.getInt(1));
        entity.setCoffeeTypeId(rs.getInt(2) );
        entity.setOrderId(rs.getInt(3) );
        entity.setQuantity(rs.getInt(4));

        return entity;
    }
}
