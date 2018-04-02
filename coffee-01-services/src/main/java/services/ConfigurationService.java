package services;

import entities.Configuration;

import java.io.Serializable;

public interface ConfigurationService {
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
    String getValue(String idKey);

    /**
     * returns an Configuration record with an id = configurationId from the database
     *
     * @param configurationId determine id of Configuration record in database
     * @return Configuration record from the database with id = configurationId, or
     *         null if such an entity was not found
     */
    Configuration get(Serializable configurationId);

    /**
     * Saves the Configuration in the database if @param configuration != null
     *
     * @param configuration determine entity with type <Configuration>
     * @return saved entity with not null id or
     *         null if configuration = null
     */
    Configuration add(Configuration configuration);

    /**
     * update an Configuration entity with an id = configuration.id in the database
     * if @param configuration != null
     *
     * @param configuration determine a new entity to be updated
     *                   in the database with id = configuration.id
     */
    void update(Configuration configuration);

    /**
     * removes from the database an Configuration entity with an id = configurationId
     *
     * @param configurationId determine id of entity in database
     * @return returns the number of deleted rows from the database
     */
    int delete(Serializable configurationId);
}
