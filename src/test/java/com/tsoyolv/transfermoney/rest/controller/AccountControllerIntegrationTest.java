package com.tsoyolv.transfermoney.rest.controller;

import com.tsoyolv.transfermoney.rest.RestPaths;
import com.tsoyolv.transfermoney.rest.webmodel.WebAccount;
import com.tsoyolv.transfermoney.rest.webmodel.WebTransaction;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class AccountControllerIntegrationTest extends AbstractControllerIntegrationTest {
    private static final String GET_ACCOUNTS_REST_PATH = RestPaths.REST_ROOT_PATH + RestPaths.ACCOUNT_ROOT_PATH;
    private static final String TRANSFER_REST_PATH = RestPaths.REST_ROOT_PATH + RestPaths.ACCOUNT_ROOT_PATH + RestPaths.TRANSFER_BETWEEN_ACCOUNTS_PATH;
    private static final String JSON_ACCOUNT_EXAMPLES_DIRECTORY_PATH = "accountcontroller" + File.separator;

    @Test
    public void testGetById() throws IOException, URISyntaxException {
        HttpResponse response = httpGet(GET_ACCOUNTS_REST_PATH + "/1", commonClient);
        assertNotNull(response);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        WebAccount webAccount = parseEntityFromHttpResponse(response, WebAccount.class);
        assertNotNull(webAccount);
        assertEquals(new BigDecimal(100).setScale(2), webAccount.getBalance());
    }

    @Test
    public void testGet() throws IOException, URISyntaxException {
        HttpResponse response = httpGet(GET_ACCOUNTS_REST_PATH, commonClient);
        assertNotNull(response);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        WebAccount[] webAccounts = parseEntityFromHttpResponse(response, WebAccount[].class);
        assertNotNull(webAccounts);
        assertEquals(6, webAccounts.length);

        WebAccount acc1 = webAccounts[0];
        WebAccount acc2 = webAccounts[1];
        WebAccount acc3 = webAccounts[2];
        WebAccount acc4 = webAccounts[3];
        WebAccount acc5 = webAccounts[3];
        WebAccount acc6 = webAccounts[3];

        assertEquals(new BigDecimal(100).setScale(2), acc1.getBalance());
        assertEquals(new BigDecimal(200).setScale(2), acc2.getBalance());
        assertEquals(new BigDecimal(500).setScale(2), acc3.getBalance());
        assertEquals(new BigDecimal(500).setScale(2), acc4.getBalance());
        assertEquals(new BigDecimal(500).setScale(2), acc5.getBalance());
        assertEquals(new BigDecimal(500).setScale(2), acc6.getBalance());
    }

    @Test
    public void testCreate() throws IOException, URISyntaxException {
        WebAccount webAccountForPost = parseJsonExampleByFileName(JSON_ACCOUNT_EXAMPLES_DIRECTORY_PATH + "createAccount.json", WebAccount.class);
        HttpResponse response = httpPost(RestPaths.REST_ROOT_PATH + RestPaths.ACCOUNT_ROOT_PATH, webAccountForPost, commonClient);
        assertNotNull(response);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        assertNotNull(response.getEntity());
        WebAccount createdAccount = parseEntityFromHttpResponse(response, WebAccount.class);
        assertNotNull(createdAccount);
        assertEquals("4123567891234567", createdAccount.getAccountNumber());
    }

    @Test
    public void testSimpleTransferringBetweenAccount() throws IOException, URISyntaxException {
        WebTransaction webTransaction1 = parseJsonExampleByFileName(JSON_ACCOUNT_EXAMPLES_DIRECTORY_PATH + "transfer1.json", WebTransaction.class);
        WebTransaction webTransaction2 = parseJsonExampleByFileName(JSON_ACCOUNT_EXAMPLES_DIRECTORY_PATH + "transfer2.json", WebTransaction.class);
        WebTransaction webTransaction3 = parseJsonExampleByFileName(JSON_ACCOUNT_EXAMPLES_DIRECTORY_PATH + "transfer3.json", WebTransaction.class);
        HttpResponse response1 = httpPost(TRANSFER_REST_PATH, webTransaction1, commonClient);
        HttpResponse response2 = httpPost(TRANSFER_REST_PATH, webTransaction2, commonClient);
        HttpResponse response3 = httpPost(TRANSFER_REST_PATH, webTransaction3, commonClient);

        assertNotNull(response1);
        assertNotNull(response2);
        assertNotNull(response3);
        assertEquals(HttpStatus.SC_OK, response1.getStatusLine().getStatusCode());
        assertEquals(HttpStatus.SC_OK, response2.getStatusLine().getStatusCode());
        assertEquals(HttpStatus.SC_OK, response3.getStatusLine().getStatusCode());

        webTransaction1 = parseJsonExampleByFileName(JSON_ACCOUNT_EXAMPLES_DIRECTORY_PATH + "transfer1revert.json", WebTransaction.class);
        webTransaction2 = parseJsonExampleByFileName(JSON_ACCOUNT_EXAMPLES_DIRECTORY_PATH + "transfer2revert.json", WebTransaction.class);
        webTransaction3 = parseJsonExampleByFileName(JSON_ACCOUNT_EXAMPLES_DIRECTORY_PATH + "transfer3revert.json", WebTransaction.class);
        response1 = httpPost(TRANSFER_REST_PATH, webTransaction1, commonClient);
        response2 = httpPost(TRANSFER_REST_PATH, webTransaction2, commonClient);
        response3 = httpPost(TRANSFER_REST_PATH, webTransaction3, commonClient);

        assertNotNull(response1);
        assertNotNull(response2);
        assertNotNull(response3);
        assertEquals(HttpStatus.SC_OK, response1.getStatusLine().getStatusCode());
        assertEquals(HttpStatus.SC_OK, response2.getStatusLine().getStatusCode());
        assertEquals(HttpStatus.SC_OK, response3.getStatusLine().getStatusCode());
    }

    @Test
    public void testMultipleThreadsTransferringBetweenAccounts() throws IOException, URISyntaxException, InterruptedException {
        int threadsAmount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadsAmount);
        for (int i = 0; i < threadsAmount; i++) {
            executor.execute(new TransferMoneyTask(new WebTransaction(1L, 2L, new BigDecimal(1L), "RUR")));
            executor.execute(new TransferMoneyTask(new WebTransaction(2L, 1L, new BigDecimal(1L), "RUR")));
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        HttpResponse response = httpGet(GET_ACCOUNTS_REST_PATH, commonClient);

        assertNotNull(response);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());

        WebAccount[] webAccounts = parseEntityFromHttpResponse(response, WebAccount[].class);

        assertEquals(webAccounts[0].getBalance(), new BigDecimal(100).setScale(2));
        assertEquals(webAccounts[1].getBalance(), new BigDecimal(200).setScale(2));
        System.out.println(Arrays.toString(webAccounts));
    }

    private class TransferMoneyTask implements Runnable {

        public TransferMoneyTask(WebTransaction webTransaction) {
            this.webTransaction = webTransaction;
        }

        private WebTransaction webTransaction;

        @Override
        public void run() {
            try {
                HttpResponse response = httpPost(TRANSFER_REST_PATH, webTransaction, commonClient);
                int statusCode = response.getStatusLine().getStatusCode();
                if (HttpStatus.SC_OK != statusCode) {
                    System.out.println("FAIL. Thread name: " + Thread.currentThread().getName());
                    throw new RuntimeException("NOT 200 " + webTransaction);
                }
                System.out.println("OK. Thread name: " + Thread.currentThread().getName());
            } catch (URISyntaxException e) {
                System.out.println("URISyntaxException " + e.getMessage());
                throw new RuntimeException("URISyntaxException ", e);
            } catch (IOException e) {
                System.out.println("IOException " + e.getMessage());
                throw new RuntimeException("IOException ", e);
            }
        }
    }
}
