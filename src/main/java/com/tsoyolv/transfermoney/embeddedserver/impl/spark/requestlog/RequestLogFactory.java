package com.tsoyolv.transfermoney.embeddedserver.impl.spark.requestlog;

import com.tsoyolv.transfermoney.log.LoggerWrapper;
import org.eclipse.jetty.server.AbstractNCSARequestLog;

public class RequestLogFactory {

    private LoggerWrapper logger;

    public RequestLogFactory(LoggerWrapper logger) {
        this.logger = logger;
    }

    AbstractNCSARequestLog create() {
        return new AbstractNCSARequestLog() {
            @Override
            protected boolean isEnabled() {
                return true;
            }

            @Override
            public void write(String s) {
                logger.info(s);
            }
        };
    }
}