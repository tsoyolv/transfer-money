package com.tsoyolv.transfermoney;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {

    private static Properties properties;
    public static String DB_DRIVER_PROPERTY_NAME = "db_driver";
    public static String DB_CONNECTION_URL_PROPERTY_NAME = "db_connection_url";
    public static String DB_USER_PROPERTY_NAME = "db_user";
    public static String DB_PASSWORD_PROPERTY_NAME = "db_password";
    public static String DB_INSERT_TEST_DATA_PROPERTY_NAME = "db_insert_test_data";
    public static String SERVER_HOST_PROPERTY_NAME = "server_host";
    public static String SERVER_PORT_PROPERTY_NAME = "server_port";
    public static String SERVER_IDLE_TIMEOUT_PROPERTY_NAME = "server_idle_timeout";

    static {
        properties = new Properties();
        final InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
        try {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Application can not be started without properties!");
        }
    }

    public static String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }
}
