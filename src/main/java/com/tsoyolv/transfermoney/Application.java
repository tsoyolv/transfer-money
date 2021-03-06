package com.tsoyolv.transfermoney;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.tsoyolv.transfermoney.database.DBMigration;
import com.tsoyolv.transfermoney.embeddedserver.EmbeddedServer;
import com.tsoyolv.transfermoney.guice.modules.MainModule;

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
            Injector injector = Guice.createInjector(new MainModule());
            embeddedServer = injector.getInstance(EmbeddedServer.class);
            embeddedServer.startServer();
            log.debug("Application successfully started");
        } catch (SQLException e) {
            log.error("Cannot load database");
            throw e;
        } catch (ClassNotFoundException e) {
            log.error("Cannot find db driver");
            throw e;
        } catch (Exception e) {
            log.error("Application start failed!", e);
            throw e;
        }
    }
}
