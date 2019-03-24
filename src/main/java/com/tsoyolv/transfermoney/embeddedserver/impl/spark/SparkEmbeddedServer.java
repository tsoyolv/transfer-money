package com.tsoyolv.transfermoney.embeddedserver.impl.spark;

import com.google.inject.Inject;
import com.tsoyolv.transfermoney.LoggerWrapper;
import com.tsoyolv.transfermoney.embeddedserver.AbstractEmbeddedServer;
import com.tsoyolv.transfermoney.embeddedserver.impl.spark.requestlog.SparkUtils;
import com.tsoyolv.transfermoney.rest.RestPaths;
import com.tsoyolv.transfermoney.rest.controller.spark.AccountController;

import static com.tsoyolv.transfermoney.LoggerWrapper.REQUESTS_LOGGER_NAME;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.stop;

/**
 * Server based on Spark framework for boot REST'full application
 */
public class SparkEmbeddedServer extends AbstractEmbeddedServer {

    private static final String ID_PATH = "/:id";
    public static final String ID = ":id";

    @Inject
    private AccountController accountController;

    /**
     * {@inheritDoc}
     */
    @Override
    public void startServer() throws Exception {
        LoggerWrapper logger = LoggerWrapper.getLogger(REQUESTS_LOGGER_NAME);
        SparkUtils.createServerWithRequestLog(logger);
        port(getServerPort());
        get(RestPaths.REST_ROOT_PATH + "/hello", (req, res) -> "Hello World");
        get(RestPaths.REST_ROOT_PATH + RestPaths.ACCOUNT_ROOT_PATH, accountController.getAccounts);
        get(RestPaths.REST_ROOT_PATH + RestPaths.ACCOUNT_ROOT_PATH + ID_PATH, accountController.getAccount);
        post(RestPaths.REST_ROOT_PATH + RestPaths.ACCOUNT_ROOT_PATH, accountController.createAccount);
    }

    @Override
    public void stopServer() throws Exception {
        stop();
    }
}
