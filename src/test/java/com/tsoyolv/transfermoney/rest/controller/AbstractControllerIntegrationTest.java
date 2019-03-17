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
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public abstract class AbstractControllerIntegrationTest {
    private static AbstractEmbeddedServer embeddedServer = new JettyEmbeddedServer(AccountController.class.getPackageName(), false);
    private static PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
    protected static HttpClient commonClient;
    private static final String ROOT_JSON_EXAMPLES_PATH = "jsonexamples" + File.separator;
    private static final String HOST = embeddedServer.getServerHost() + ":" + embeddedServer.getServerPort();
    private static final String URI_SCHEME = "http";

    private ObjectMapper mapper = new ObjectMapper();

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
        URI uri = new URIBuilder().setScheme(URI_SCHEME).setHost(HOST).setPath(url).build();
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
        URI uri = new URIBuilder().setScheme(URI_SCHEME).setHost(HOST).setPath(url).build();
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

    protected <T> T parseEntityFromHttpResponse(HttpResponse response, Class<T> t) throws IOException {
        String jsonString = EntityUtils.toString(response.getEntity());
        return mapper.readValue(jsonString, t);
    }

    protected <T> T parseJsonExampleByFileName(String fileName, Class<T> clazz) throws IOException {
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(ROOT_JSON_EXAMPLES_PATH + fileName);
        Scanner scanner = new Scanner(resourceAsStream).useDelimiter("\\A");
        String json = scanner.hasNext() ? scanner.next() : "";
        return mapper.readValue(json, clazz);
    }
}