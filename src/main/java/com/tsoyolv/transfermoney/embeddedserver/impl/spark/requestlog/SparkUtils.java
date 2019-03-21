package com.tsoyolv.transfermoney.embeddedserver.impl.spark.requestlog;

import com.tsoyolv.transfermoney.log.LoggerWrapper;
import org.eclipse.jetty.server.AbstractNCSARequestLog;
import spark.embeddedserver.EmbeddedServers;
import spark.embeddedserver.jetty.EmbeddedJettyFactory;

public class SparkUtils {
    public static void createServerWithRequestLog(LoggerWrapper logger) {
        EmbeddedJettyFactory factory = createEmbeddedJettyFactoryWithRequestLog(logger);
        EmbeddedServers.add(EmbeddedServers.Identifiers.JETTY, factory);
    }

    private static EmbeddedJettyFactory createEmbeddedJettyFactoryWithRequestLog(LoggerWrapper logger) {
        AbstractNCSARequestLog requestLog = new RequestLogFactory(logger).create();
        return new EmbeddedJettyFactoryConstructor(requestLog).create();
    }
}