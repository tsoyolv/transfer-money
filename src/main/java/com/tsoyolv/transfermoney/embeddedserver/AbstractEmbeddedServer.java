package com.tsoyolv.transfermoney.embeddedserver;

import com.tsoyolv.transfermoney.ApplicationProperties;

import static com.tsoyolv.transfermoney.ApplicationProperties.SERVER_HOST_PROPERTY_NAME;
import static com.tsoyolv.transfermoney.ApplicationProperties.SERVER_IDLE_TIMEOUT_PROPERTY_NAME;
import static com.tsoyolv.transfermoney.ApplicationProperties.SERVER_PORT_PROPERTY_NAME;

/**
 * Abstract embedded server
 */
public abstract class AbstractEmbeddedServer {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;
    private static final int DEFAULT_IDLE_TIMEOUT = 30000;

    /**
     * Starts embedded server
     * @throws Exception if server can't start
     */
    public abstract void startServer() throws Exception;

    public String getServerHost() {
        String serverHost = ApplicationProperties.getProperty(SERVER_HOST_PROPERTY_NAME);
        if (serverHost == null) {
            return DEFAULT_HOST;
        }
        return serverHost;
    }

    public Integer getServerPort() {
        String serverPortStr = ApplicationProperties.getProperty(SERVER_PORT_PROPERTY_NAME);
        if (serverPortStr == null) {
            return DEFAULT_PORT;
        }
        return Integer.parseInt(serverPortStr);
    }

    protected long getServerIdleTimeout() {
        String serverIdleTimeout = ApplicationProperties.getProperty(SERVER_IDLE_TIMEOUT_PROPERTY_NAME);
        if (serverIdleTimeout == null) {
            return DEFAULT_IDLE_TIMEOUT;
        }
        return Long.parseLong(serverIdleTimeout);
    }
}
