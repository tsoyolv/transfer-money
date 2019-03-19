package com.tsoyolv.transfermoney.embeddedserver.impl.spark;

import org.apache.log4j.Logger;
import com.tsoyolv.transfermoney.embeddedserver.impl.AbstractEmbeddedServer;
import com.tsoyolv.transfermoney.embeddedserver.impl.spark.requestlog.SparkUtils;
import spark.Spark;

import javax.ws.rs.NotSupportedException;

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
        Spark.port(getServerPort());
        Spark.get("/hello", (req, res) -> "Hello World");
    }

    @Override
    public void stopServer() throws Exception {
        throw new NotSupportedException("not implemented yet");
    }
}
