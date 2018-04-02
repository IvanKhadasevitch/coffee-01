package services.impl;

import dao.ConfigurationDao;
import entities.Configuration;
import org.apache.log4j.Logger;
import services.ConfigurationService;
import services.ServiceException;
import utils.SingletonBuilder;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationServiceImpl extends AbstractService implements ConfigurationService {
    private static Logger log = Logger.getLogger(ConfigurationServiceImpl.class);
    private ConfigurationDao configurationDao = SingletonBuilder.getInstanceImpl(ConfigurationDao.class);

    private final Map<String,String> DEFAULT_CONFIGURATION = new HashMap<>();
    {
        DEFAULT_CONFIGURATION.put("n","5");
        DEFAULT_CONFIGURATION.put("x","10");
        DEFAULT_CONFIGURATION.put("m","2");
    }

    private ConfigurationServiceImpl() {}

    /**
     * Returns the configuration value for the key specified by the parameter idKey
     * from the database. If there is no configuration in the database,
     * returns the default configuration for the specified key.
     *
     * @param idKey defines a key in the database to find the configuration
     * @return the configuration value for the key specified by the parameter idKey
     *         from the database. If there is no configuration in the database,
     *         returns the default configuration for the specified key, or null
     *         if not in the database and there is no default configuration
     */
    @Override
    public String getValue(String idKey) {
        try {
            String configurationValue = null;
            Configuration configuration = configurationDao.get(idKey);
            if (configuration == null) {
                configurationValue = DEFAULT_CONFIGURATION.get(idKey);
            } else {
                configurationValue = configuration.getValue();
            }
            // "n" configuration can't be zero
            if ("n".equals(idKey) && Integer.valueOf(configurationValue) == 0) {
                configurationValue = DEFAULT_CONFIGURATION.get(idKey);
            }

            return configurationValue;
        } catch (SQLException e) {
            String errorMessage = "Error getting Configuration by id: " + idKey;
            log.error(errorMessage + e.getMessage());
            throw new ServiceException(errorMessage);
        }
    }

    /**
     * returns an Configuration record with an id = configurationId from the database
     *
     * @param configurationId determine id of Configuration record in database
     * @return Configuration record from the database with id = configurationId, or
     *         null if such an entity was not found
     */
    @Override
    public Configuration get(Serializable configurationId) {
        try {
            return configurationDao.get(configurationId);
        } catch (SQLException e) {
            String errorMessage = "Error getting Configuration by id: " + configurationId;
            log.error(errorMessage + e.getMessage());
            throw new ServiceException(errorMessage);
        }
    }

    /**
     * Saves the Configuration in the database if @param configuration != null
     *
     * @param configuration determine entity with type <Configuration>
     * @return saved entity with not null id or
     *         null if configuration = null
     */
    @Override
    public Configuration add(Configuration configuration) {
        if (configuration == null) {
            return null;
        }
        try {
            this.startTransaction();
            Configuration configurationSave = configurationDao.save(configuration);
            this.commit();
            this.stopTransaction();

            return configurationSave;
        } catch (SQLException e) {
            rollback();
            String errorMessage = "Error saving Configuration: " + configuration;
            log.error(errorMessage + e.getMessage());
            throw new ServiceException(errorMessage);
        }
    }

    /**
     * update an Configuration entity with an id = configuration.id in the database
     * if @param configuration != null
     *
     * @param configuration determine a new entity to be updated
     *                      in the database with id = configuration.id
     */
    @Override
    public void update(Configuration configuration) {
        if (configuration == null) {
            return;
        }
        try {
            this.startTransaction();
            configurationDao.update(configuration);
            this.commit();
            this.stopTransaction();
        } catch (SQLException e) {
            rollback();
            String errorMessage = "Error updating Configuration: " + configuration;
            log.error(errorMessage + e.getMessage());
            throw new ServiceException(errorMessage);
        }
    }

    /**
     * removes from the database an Configuration entity with an id = configurationId
     *
     * @param configurationId determine id of entity in database
     * @return returns the number of deleted rows from the database
     */
    @Override
    public int delete(Serializable configurationId) {
        try {
            this.startTransaction();
            int deletedRecords = configurationDao.delete(configurationId);
            this.commit();
            this.stopTransaction();

            return deletedRecords;
        } catch (SQLException e) {
            rollback();
            String errorMessage = "Error deleting from DB Configuration with id: " + configurationId;
            log.error(errorMessage + e.getMessage());
            throw new ServiceException(errorMessage);
        }
    }
}
