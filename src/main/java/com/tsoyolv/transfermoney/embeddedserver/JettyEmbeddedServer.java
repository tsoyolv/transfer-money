package com.tsoyolv.transfermoney.embeddedserver;

import com.tsoyolv.transfermoney.UriPath;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Jetty-server for boot REST'full application
 */
public class JettyEmbeddedServer extends AbstractEmbeddedServer {

    private String packageForHttpControllers;

    private boolean joinServer;

    public JettyEmbeddedServer(String packageForHttpControllers, boolean joinServer) {
        this.packageForHttpControllers = packageForHttpControllers;
        this.joinServer = joinServer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startServer() throws Exception {
        Server server = new Server();
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

    private ServerConnector createServerConnector(Server server) {
        ServerConnector serverConnector = new ServerConnector(server);
        serverConnector.setHost(getServerHost());
        serverConnector.setPort(getServerPort());
        serverConnector.setIdleTimeout(getServerIdleTimeout());
        return serverConnector;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        servletContextHandler.setContextPath(UriPath.SERVER_ROOT_PATH);
        ServletHolder servletHolder = servletContextHandler.addServlet(ServletContainer.class, UriPath.REST_ROOT_PATH + "/*");
        servletHolder.setInitOrder(1);
        servletHolder.setInitParameter("jersey.config.server.provider.packages", packageForHttpControllers);
        return servletContextHandler;
    }
}
