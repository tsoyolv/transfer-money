package com.tsoyolv.transfermoney.embeddedserver.impl;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Jetty-server for boot REST'full application
 */
public class JettyEmbeddedServer extends AbstractEmbeddedServer {

    private static final String REST_ROOT_PATH = "/rest";
    private String packageForHttpControllers;

    private boolean joinServer;

    private Server server = new Server();

    public JettyEmbeddedServer(String packageForHttpControllers, boolean joinServer) {
        this.packageForHttpControllers = packageForHttpControllers;
        this.joinServer = joinServer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startServer() throws Exception {
        server.addConnector(createServerConnector(server));
        server.setHandler(createServletContextHandler());
        try {
            server.start();
            if (joinServer) {
                server.join();
            }
        } finally {
            if (joinServer) {
                server.destroy();
            }
        }
    }

    @Override
    public void stopServer() throws Exception {
        server.stop();
        server.destroy();
    }

    private ServerConnector createServerConnector(Server server) {
        ServerConnector serverConnector = new ServerConnector(server);
        serverConnector.setHost(getServerHost());
        serverConnector.setPort(getServerPort());
        serverConnector.setIdleTimeout(getServerIdleTimeout());
        return serverConnector;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        servletContextHandler.setContextPath(getServerRootPath());
        ServletHolder servletHolder = servletContextHandler.addServlet(ServletContainer.class, REST_ROOT_PATH + "/*");
        servletHolder.setInitOrder(1);
        servletHolder.setInitParameter("jersey.config.server.provider.packages", packageForHttpControllers);
        return servletContextHandler;
    }
}
