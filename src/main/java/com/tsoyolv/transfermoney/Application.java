package com.tsoyolv.transfermoney;

import com.tsoyolv.transfermoney.database.DBMigration;
import com.tsoyolv.transfermoney.embeddedserver.impl.JettyEmbeddedServer;
import com.tsoyolv.transfermoney.rest.controller.AccountController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

/**
 * Main class. Starts application with application.properties
 */
public class Application {

    private static final Logger log = LogManager.getLogger(Application.class);

    public static void main(String... args) throws Exception {
        try {
            DBMigration.runSqlScripts();
            // get package for http controllers
            String packageForHttpControllers = AccountController.class.getPackageName();
            new JettyEmbeddedServer(packageForHttpControllers, true).startServer();
            log.debug("Application successfully started");
        } catch (SQLException e) {
            log.error("Cannot load database");
            throw e;
        } catch (ClassNotFoundException e) {
            log.error("Cannot find db driver");
            throw e;
        }
    }
}
