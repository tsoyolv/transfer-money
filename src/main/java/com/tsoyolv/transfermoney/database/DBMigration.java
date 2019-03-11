package com.tsoyolv.transfermoney.database;

import com.tsoyolv.transfermoney.ApplicationProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.internal.scanning.ResourceFinderException;
import org.h2.tools.RunScript;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

import static com.tsoyolv.transfermoney.ApplicationProperties.DB_INSERT_TEST_DATA_PROPERTY_NAME;

/**
 * Run sql scripts to create tables
 */
public class DBMigration {

    private static final Logger log = LogManager.getLogger(DBMigration.class);
    private static String ROOT_DB_MIGRATION = "dbmigration" + File.separator;;

    /**
     * Run ddl scripts (and dml if test data needed)
     * @throws SQLException
     */
    public static void runSqlScripts() throws SQLException {
        runSqlScript("create_tables.sql");
        insertTestData();
    }

    private static void insertTestData() throws SQLException {
        if (Boolean.parseBoolean(ApplicationProperties.getProperty(DB_INSERT_TEST_DATA_PROPERTY_NAME))) {
            runSqlScript("insert_test_data_into_tables.sql");
        }
    }

    private static void runSqlScript(String scriptName) throws SQLException {
        try (Connection conn = DatabaseConnector.getConnection()) {
            RunScript.execute(conn, getScriptAsInputStreamReader(ROOT_DB_MIGRATION + scriptName));
        } catch (SQLException e) {
            log.error("Db migration for script: " + scriptName + " failed", e);
            throw e;
        } catch (ResourceFinderException e) {
            log.error("There is no script with name: " + scriptName, e);
            throw e;
        }
    }

    private static InputStreamReader getScriptAsInputStreamReader(String scriptName) throws ResourceFinderException {
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(scriptName);
        if (resourceAsStream == null) {
            throw new ResourceFinderException("");
        }
        return new InputStreamReader(resourceAsStream);
    }
}
