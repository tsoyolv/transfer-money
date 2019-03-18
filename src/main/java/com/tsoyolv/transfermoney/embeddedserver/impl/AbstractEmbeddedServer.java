package com.tsoyolv.transfermoney.embeddedserver.impl;

import com.tsoyolv.transfermoney.ApplicationProperties;
import com.tsoyolv.transfermoney.embeddedserver.EmbeddedServer;

import static com.tsoyolv.transfermoney.ApplicationProperties.SERVER_HOST_PROPERTY_NAME;
import static com.tsoyolv.transfermoney.ApplicationProperties.SERVER_IDLE_TIMEOUT_PROPERTY_NAME;
import static com.tsoyolv.transfermoney.ApplicationProperties.SERVER_PORT_PROPERTY_NAME;

/**
 * Abstract embedded server
 */
public abstract class AbstractEmbeddedServer implements EmbeddedServer {
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_SERVER_ROOT_PATH = "/";
    private static final int DEFAULT_PORT = 8080;
    private static final long DEFAULT_IDLE_TIMEOUT = 30000;

    @Override
    public String getServerHost() {
        String serverHost = ApplicationProperties.getProperty(SERVER_HOST_PROPERTY_NAME);
        if (serverHost == null) {
            return DEFAULT_HOST;
        }
        return serverHost;
    }

    @Override
    public Integer getServerPort() {
        String serverPortStr = ApplicationProperties.getProperty(SERVER_PORT_PROPERTY_NAME);
        if (serverPortStr == null) {
            return DEFAULT_PORT;
        }
        return Integer.parseInt(serverPortStr);
    }

    @Override
    public Long getServerIdleTimeout() {
        String serverIdleTimeout = ApplicationProperties.getProperty(SERVER_IDLE_TIMEOUT_PROPERTY_NAME);
        if (serverIdleTimeout == null) {
            return DEFAULT_IDLE_TIMEOUT;
        }
        return Long.parseLong(serverIdleTimeout);
    }

    @Override
    public String getServerRootPath() {
        return DEFAULT_SERVER_ROOT_PATH;
    }
}
