package com.tsoyolv.transfermoney.embeddedserver;

public interface EmbeddedServer {

    /**
     * Starts embedded server
     * @throws Exception if server can't start
     */
    void startServer() throws Exception;

    String getServerHost();

    Integer getServerPort() ;

    Long getServerIdleTimeout();

    String getServerRootPath();
}
