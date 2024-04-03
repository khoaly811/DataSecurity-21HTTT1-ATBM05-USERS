package DataAccessLayer;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
//import java.sql.ResultSet;
import java.sql.SQLException;

public class DataAccessLayer {
    private static volatile DataAccessLayer instance;
    private static Connection conn;
    private static Driver driver;
    //private static ResultSet rs;

    private DataAccessLayer(String username, String password) throws SQLException {
        // Initialize the database connection
        driver = new oracle.jdbc.OracleDriver();
        DriverManager.registerDriver(driver);
        conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/ATBM2024", username, password);
    }

    public static DataAccessLayer getInstance(String username, String password) throws SQLException {
        // Singleton pattern to ensure only one instance of DataAccessLayer is created
        DataAccessLayer result = instance;
        if (result == null) {
            synchronized (DataAccessLayer.class) {
                result = instance;
                if (result == null) {
                    instance = result = new DataAccessLayer(username, password);
                }
            } 
        }
        return result;
    }

    public Connection connect() {
        // Get the database connection
        return conn;
    }

    public void disconnect() throws SQLException {
        DriverManager.deregisterDriver(driver);
    }

}
