package db;

import org.apache.log4j.Logger;

import java.sql.Connection;

public class ConnectionManager {
    private static Logger log = Logger.getLogger(ConnectionManager.class);
    private static ThreadLocal<Connection> tl = new ThreadLocal<>();

    public static Connection getConnection() throws DbManagerException {
        try {
            if (tl.get() == null) {
                tl.set(DataSource.getInstance().getConnection());
            }
            return tl.get();
        } catch (Exception e) {
            log.error("Error getting connection " + e.getMessage());
            throw new DbManagerException("Error getting connection " +  e.getMessage());
        }
    }
}
