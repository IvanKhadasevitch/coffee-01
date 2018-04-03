package dao;

import java.io.Serializable;
import java.sql.SQLException;

public interface DAO<T> {
    /**
     *
     * Saves the entity type <T> in the database if @param t != null
     *
     * @param t determine entity with type <T>
     * @return saved entity with not null id or
     *         null if t = null
     * @throws SQLException if can't save entity
     */
    T save(T t) throws SQLException;

    /**
     *
     * returns an entity with an id from the database
     *
     * @param id determine id of entity in database
     * @return entity with type <T> from the database, or null if such an entity was not found
     * @throws SQLException if there is an error connecting to the database
     */
    T get(Serializable id) throws SQLException;

    /**
     *
     * update an entity with an id = t.id in the database if @param t != null
     *
     * @param t determine a new entity to be updated in the database with id = t.id
     * @throws SQLException if there is an error updating entity in the database
     */
    void update(T t) throws SQLException;

    /**
     *
     * removes from the database an entity with an id
     *
     * @param id determine id of entity in database
     * @return returns the number of deleted rows from the database
     * @throws SQLException if there is an error deleting entity from the database
     */
    int delete(Serializable id) throws SQLException;
}
