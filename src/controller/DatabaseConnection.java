//DatabaseConnection.java
package controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static Connection getConnection() {
        Connection conn = null;
        final String DB_URL = "jdbc:mysql://localhost/istore?serverTimezone=UTC&user=root&password=";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }
}
