package org.main.API;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLAPI {

    private static final String DATABASE_URL = "jdbc:mariadb://10.10.1.31:3306/siecmc_main";
    private static final String DATABASE_USER = "siecmc_master";
    private static final String DATABASE_PASSWORD = "o9ubq9gn3o40ibg50qni4nh50g3n5hb";
    private static Connection connection;

    public static void connect() throws SQLException, ClassNotFoundException {
        if (connection == null || connection.isClosed()) {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        }
    }

    public static void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }
}

