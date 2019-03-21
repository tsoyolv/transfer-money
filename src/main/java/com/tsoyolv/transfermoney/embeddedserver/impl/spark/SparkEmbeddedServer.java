package com.tsoyolv.transfermoney.embeddedserver.impl.spark;

import com.tsoyolv.transfermoney.embeddedserver.AbstractEmbeddedServer;
import com.tsoyolv.transfermoney.embeddedserver.impl.spark.requestlog.SparkUtils;
import com.tsoyolv.transfermoney.LoggerWrapper;
import com.tsoyolv.transfermoney.rest.controller.spark.AccountController;

import static com.tsoyolv.transfermoney.LoggerWrapper.REQUESTS_LOGGER_NAME;
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
        LoggerWrapper logger = LoggerWrapper.getLogger(REQUESTS_LOGGER_NAME);
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
