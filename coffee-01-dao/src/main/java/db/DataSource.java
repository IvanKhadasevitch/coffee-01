package db;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DataSource {
    private static volatile DataSource INSTANCE = null;
    private ComboPooledDataSource pooledDatasource;

    private final String URL;
    private final String DRIVER;
    private final String USER;
    private final String PASSWORD;

    {
        ResourceBundle rb = ResourceBundle.getBundle("db_coffee");
        if (rb == null) {
            URL = "UNDEFINED";
            USER = "UNDEFINED";
            PASSWORD = "UNDEFINED";
            DRIVER = "com.mysql.jdbc.Driver";
            System.out.println("Bundle for db was not initialized");
        } else {
            URL = rb.getString("url");
            USER = rb.getString("login");
            PASSWORD = rb.getString("password");
            DRIVER = rb.getString("driver");
        }
    }

    private DataSource() throws IOException, SQLException, PropertyVetoException {
        pooledDatasource = new ComboPooledDataSource();
        pooledDatasource.setDriverClass(DRIVER); //loads the jdbc driver
        pooledDatasource.setJdbcUrl(URL);
        pooledDatasource.setUser(USER);
        pooledDatasource.setPassword(PASSWORD);

        // the settings below are optional -- c3p0 can work with defaults
        pooledDatasource.setMinPoolSize(10);
        pooledDatasource.setAcquireIncrement(5);
        pooledDatasource.setMaxPoolSize(20);
        pooledDatasource.setMaxStatements(180);
        pooledDatasource.setInitialPoolSize(10);

    }

    public static DataSource getInstance() throws PropertyVetoException, SQLException, IOException {
        DataSource instance = INSTANCE;
        if (instance == null) {
            synchronized (DataSource.class) {
                instance = INSTANCE;
                if (instance == null) {
                    INSTANCE = instance = new DataSource();
                }
            }
        }

        return instance;
    }

    public Connection getConnection() throws SQLException {
        return pooledDatasource.getConnection();
    }
}

