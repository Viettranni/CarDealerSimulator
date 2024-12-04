package simu.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDbConnection {
    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            // connect if necessary
            try {
                conn = DriverManager.getConnection(
                        "jdbc:mariadb://localhost:3306/car_sim_db?user=saed&password=12345678");
            } catch (SQLException e) {
                System.out.println("Connection failed.");
                e.printStackTrace();
            }
            return conn;
        } else {
            return conn;
        }
    }

    public static void terminate() {
        try {
            getConnection().close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}