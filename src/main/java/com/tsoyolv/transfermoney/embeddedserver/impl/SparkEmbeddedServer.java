package com.tsoyolv.transfermoney.embeddedserver.impl;

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
        Spark.port(getServerPort());
        Spark.get("/hello", (req, res) -> "Hello World");
    }

    @Override
    public void stopServer() throws Exception {
        throw new NotSupportedException("not implemented yet");
    }
}
