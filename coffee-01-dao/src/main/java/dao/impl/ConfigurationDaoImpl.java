package dao.impl;

import dao.ConfigurationDao;
import entities.Configuration;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfigurationDaoImpl extends AbstractDao implements ConfigurationDao {
    private static Logger log = Logger.getLogger(ConfigurationDaoImpl.class);

    private static final String SAVE_CONFIGURATION_SQL =
            "INSERT INTO Configuration (id, `value`) VALUES (?,?)";
    private static final String GET_CONFIGURATION_BY_ID_SQL =
            "SELECT * FROM Configuration WHERE id=?";
    private static final String UPDATE_CONFIGURATION_BY_ID_SQL =
            "UPDATE Configuration SET `value`=? WHERE id=?";
    private static final String DELETE_CONFIGURATION_BY_ID_SQL =
            "DELETE FROM Configuration WHERE id=?";

    private PreparedStatement psSave = null;
    private PreparedStatement psGet = null;
    private PreparedStatement psUpdate = null;
    private PreparedStatement psDelete = null;

    private ConfigurationDaoImpl() {}

    /**
     * Saves the entity type <Configuration> in the database
     *
     * @param configuration determine entity with type <Configuration>
     * @return saved entity with not null id or
     *         null if configuration is null or configuration.id is null
     * @throws SQLException if can't save entity
     */
    @Override
    public Configuration save(Configuration configuration) throws SQLException {

        if (configuration != null && configuration.getId() != null) {
            try {
                if (psSave == null) {
                    psSave = psSave = prepareStatement(SAVE_CONFIGURATION_SQL);
                }
                psSave.setString(1, configuration.getId());
                psSave.setString(2, configuration.getValue());
                psSave.executeUpdate();
            } catch (SQLException e) {
                String errorMessage = "Can't execute SQL: " + psSave + e.getMessage();
                log.error(errorMessage);
                throw new SQLException(errorMessage);
            }

            return configuration;
        } else {

            return null;
        }
    }

    /**
     * returns an entity with an id from the database
     *
     * @param id determine id of entity in database
     * @return entity with type <Configuration> from the database,
     *         or null if such an entity was not found
     * @throws SQLException if there is an error connecting to the database
     */
    @Override
    public Configuration get(Serializable id) throws SQLException {
        if (psGet == null) {
            psGet = prepareStatement(GET_CONFIGURATION_BY_ID_SQL);
        }
        psGet.setString(1, (String) id);
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
     * update an entity with an id = configuration.id in the database
     *
     * @param configuration determine a new entity to be updated
     *                    in the database with id = configuration.id
     * @throws SQLException if there is an error updating entity in the database
     */
    @Override
    public void update(Configuration configuration) throws SQLException {
        if (configuration == null) {
            return;
        }
        try {
            if (psUpdate == null) {
                psUpdate = prepareStatement(UPDATE_CONFIGURATION_BY_ID_SQL);
            }
            psUpdate.setString(2, configuration.getId());
            psUpdate.setString(1, configuration.getValue());
            psUpdate.executeUpdate();
        } catch (SQLException e) {
            String errorMessage = "Can't execute SQL: " + psUpdate + e.getMessage();
            log.error(errorMessage);
            throw new SQLException(errorMessage);
        }
    }

    /**
     * removes from the database an entity with type <Configuration> and id
     *
     * @param id determine id of entity in database
     * @return returns the number of deleted rows from the database
     * @throws SQLException if there is an error deleting entity from the database
     */
    @Override
    public int delete(Serializable id) throws SQLException {
        try {
            if (psDelete == null) {
                psDelete = prepareStatement(DELETE_CONFIGURATION_BY_ID_SQL);
            }
            psDelete.setString(1, (String) id);
            return psDelete.executeUpdate();
        } catch (SQLException e) {
            String errorMessage = "Can't execute SQL: " + psDelete + e.getMessage();
            log.error(errorMessage);
            throw new SQLException(errorMessage);
        }
    }

    private Configuration populateEntity(ResultSet rs) throws SQLException {
        Configuration entity = new Configuration();
        entity.setId(rs.getString(1));
        entity.setValue(rs.getString(2) );

        return entity;
    }
}
