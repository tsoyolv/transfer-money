package com.tsoyolv.transfermoney;

import com.tsoyolv.transfermoney.database.DBMigration;
import com.tsoyolv.transfermoney.embeddedserver.EmbeddedServer;
import com.tsoyolv.transfermoney.embeddedserver.impl.spark.SparkEmbeddedServer;
import com.tsoyolv.transfermoney.log.LoggerWrapper;

import java.sql.SQLException;

/**
 * Main class. Starts application with application.properties
 */
public class Application {

    private static final LoggerWrapper log = LoggerWrapper.getLogger(Application.class);

    private static EmbeddedServer embeddedServer;

    public static void main(String... args) throws Exception {
        try {
            DBMigration.runSqlScripts();
            // get package for http controllers
            /*String packageForHttpControllers = AccountController.class.getPackageName();
            embeddedServer = new JettyEmbeddedServer(packageForHttpControllers, true);
            embeddedServer.startServer();*/
            embeddedServer = new SparkEmbeddedServer();
            embeddedServer.startServer();
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
