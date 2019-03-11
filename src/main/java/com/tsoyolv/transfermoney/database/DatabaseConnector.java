package com.tsoyolv.transfermoney.database;

import com.tsoyolv.transfermoney.ApplicationProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.tsoyolv.transfermoney.ApplicationProperties.DB_CONNECTION_URL_PROPERTY_NAME;
import static com.tsoyolv.transfermoney.ApplicationProperties.DB_DRIVER_PROPERTY_NAME;
import static com.tsoyolv.transfermoney.ApplicationProperties.DB_PASSWORD_PROPERTY_NAME;
import static com.tsoyolv.transfermoney.ApplicationProperties.DB_USER_PROPERTY_NAME;

/**
 * Connection for database
 */
public class DatabaseConnector {
    static {
        try {
            Class.forName(ApplicationProperties.getProperty(DB_DRIVER_PROPERTY_NAME));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get db connection (application.properties)
     * @return Connection
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(ApplicationProperties.getProperty(DB_CONNECTION_URL_PROPERTY_NAME),
                ApplicationProperties.getProperty(DB_USER_PROPERTY_NAME), ApplicationProperties.getProperty(DB_PASSWORD_PROPERTY_NAME));
    }
}
