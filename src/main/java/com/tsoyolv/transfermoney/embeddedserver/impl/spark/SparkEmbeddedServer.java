package com.tsoyolv.transfermoney.embeddedserver.impl.spark;

import com.tsoyolv.transfermoney.embeddedserver.impl.AbstractEmbeddedServer;
import com.tsoyolv.transfermoney.embeddedserver.impl.spark.requestlog.SparkUtils;
import com.tsoyolv.transfermoney.rest.controller.spark.AccountController;
import org.apache.log4j.Logger;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.stop;

/**
 * Server based on Spark framework for boot REST'full application
 */
public class SparkEmbeddedServer extends AbstractEmbeddedServer {

    /**
     * {@inheritDoc}
     */
    @Override
    public void startServer() throws Exception {
        Logger logger = Logger.getLogger(SparkEmbeddedServer.class);
        SparkUtils.createServerWithRequestLog(logger);
        port(getServerPort());
        get("/hello", (req, res) -> "Hello World");
        get("/account", AccountController.getAccounts);
    }

    @Override
    public void stopServer() throws Exception {
        stop();
    }
}
