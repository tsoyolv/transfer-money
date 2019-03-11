package com.tsoyolv.transfermoney.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsoyolv.transfermoney.database.DBMigration;
import com.tsoyolv.transfermoney.embeddedserver.AbstractEmbeddedServer;
import com.tsoyolv.transfermoney.embeddedserver.JettyEmbeddedServer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public abstract class AbstractControllerTest {
    private static AbstractEmbeddedServer embeddedServer = new JettyEmbeddedServer(AccountController.class.getPackageName(), false);
    private static PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
    protected static final String HOST = embeddedServer.getServerHost() + ":" + embeddedServer.getServerPort();
    protected static HttpClient commonClient;
    protected ObjectMapper mapper = new ObjectMapper();
    protected URIBuilder builder = new URIBuilder().setScheme("http").setHost(HOST);

    @BeforeClass
    public static void setup() throws Exception {
        DBMigration.runSqlScripts();
        embeddedServer.startServer();
        connManager.setDefaultMaxPerRoute(100);
        connManager.setMaxTotal(200);
        commonClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setConnectionManagerShared(true)
                .build();
    }

    @AfterClass
    public static void closeClient() {
        HttpClientUtils.closeQuietly(commonClient);
    }

    protected HttpResponse httpGet(String url, HttpClient client) throws URISyntaxException, IOException {
        URI uri = new URIBuilder().setScheme("http").setHost(HOST).setPath(url).build();
        HttpGet request = new HttpGet(uri);
        return client.execute(request);
    }

    protected HttpResponse httpGetWithNewClient(String url) throws URISyntaxException, IOException {
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setDefaultMaxPerRoute(100);
        connManager.setMaxTotal(200);
        HttpClient client = HttpClients.custom()
                .setConnectionManager(connManager)
                .setConnectionManagerShared(true)
                .build();
        return httpGet(url, client);
    }

    protected HttpResponse httpPost(String url, Object entity, HttpClient client) throws URISyntaxException, IOException {
        URI uri = new URIBuilder().setScheme("http").setHost(HOST).setPath(url).build();
        String jsonInString = mapper.writeValueAsString(entity);
        StringEntity strEntity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", MediaType.APPLICATION_JSON);
        request.setEntity(strEntity);
        return client.execute(request);
    }

    protected HttpResponse httpPostWithNewClient(String url, Object entity) throws URISyntaxException, IOException {
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setDefaultMaxPerRoute(100);
        connManager.setMaxTotal(200);
        HttpClient client = HttpClients.custom()
                .setConnectionManager(connManager)
                .setConnectionManagerShared(true)
                .build();
        return httpPost(url, entity, client);
    }
}
