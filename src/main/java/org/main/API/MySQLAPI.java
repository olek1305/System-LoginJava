package org.main.API;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLAPI {

    private static final String DATABASE_URL = "jdbc:mariadb://54.38.50.59:3306/www11799_arcyrpg";
    private static final String DATABASE_USER = "www11799_arcyrpg";
    private static final String DATABASE_PASSWORD = "TtmtDuPK9xR6h7GAVlcN";
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

