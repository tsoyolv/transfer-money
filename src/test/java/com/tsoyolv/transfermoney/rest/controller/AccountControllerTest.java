package com.tsoyolv.transfermoney.rest.controller;

import com.tsoyolv.transfermoney.UriPath;
import com.tsoyolv.transfermoney.rest.webmodel.WebAccount;
import com.tsoyolv.transfermoney.rest.webmodel.WebTransaction;
import org.apache.http.HttpResponse;
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


public class AccountControllerTest extends AbstractControllerTest {
    private static final String GET_ACCOUNTS = UriPath.REST_ROOT_PATH + UriPath.ACCOUNT_ROOT_PATH;
    private static final String TRANSFER = UriPath.REST_ROOT_PATH + UriPath.ACCOUNT_ROOT_PATH + UriPath.TRANSFER_BETWEEN_ACCOUNTS_PATH;
    private static final String JSON_ACCOUNT_EXAMPLES_PATH = "accountcontroller" + File.separator;

    @Test
    public void testCreate() throws IOException, URISyntaxException {
        WebAccount webAccountForPost = parseJsonExampleByFileName(JSON_ACCOUNT_EXAMPLES_PATH + "createAccount.json", WebAccount.class);
        HttpResponse response = httpPost(UriPath.REST_ROOT_PATH + UriPath.ACCOUNT_ROOT_PATH, webAccountForPost, commonClient);
        assertNotNull(response);
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertNotNull(response.getEntity());
        WebAccount createdAccount = parseEntityFromHttpResponse(response, WebAccount.class);
        assertNotNull(createdAccount);
        assertEquals("4123567891234567", createdAccount.getAccountNumber());
    }

    @Test
    public void testSimpleTransferringBetweenAccount() throws IOException, URISyntaxException {
        WebTransaction webTransaction1 = parseJsonExampleByFileName(JSON_ACCOUNT_EXAMPLES_PATH + "transfer1.json", WebTransaction.class);
        WebTransaction webTransaction2 = parseJsonExampleByFileName(JSON_ACCOUNT_EXAMPLES_PATH + "transfer2.json", WebTransaction.class);
        WebTransaction webTransaction3 = parseJsonExampleByFileName(JSON_ACCOUNT_EXAMPLES_PATH + "transfer3.json", WebTransaction.class);

        HttpResponse response = transferMoneyPostRequest(webTransaction1);
        HttpResponse response1 = transferMoneyPostRequest(webTransaction2);
        HttpResponse response2 = transferMoneyPostRequest(webTransaction3);

        assertNotNull(response);
        assertNotNull(response1);
        assertNotNull(response2);
        assertEquals(response.getStatusLine().getStatusCode(), 200);
        assertEquals(response1.getStatusLine().getStatusCode(), 200);
        assertEquals(response2.getStatusLine().getStatusCode(), 200);

        WebAccount[] webAccounts = getWebAccountsGetRequest();
        WebAccount acc1 = webAccounts[0];
        WebAccount acc2 = webAccounts[1];
        WebAccount acc3 = webAccounts[2];
        WebAccount acc4 = webAccounts[3];

        assertEquals(acc1.getBalance(), new BigDecimal(44).setScale(2));
        assertEquals(acc2.getBalance(), new BigDecimal(233).setScale(2));
        assertEquals(acc3.getBalance(), new BigDecimal(479).setScale(2));
        assertEquals(acc4.getBalance(), new BigDecimal(544).setScale(2));
        System.out.println(webAccounts);
    }

    // todo doesn't work normally
    @Test
    public void testMultipleThreadsTransferring() throws IOException, URISyntaxException, InterruptedException {
        int threadsAmount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadsAmount);
        WebTransaction webTransaction1 = new WebTransaction(1L, 2L, new BigDecimal(1L), "RUR");
        WebTransaction webTransaction4 = new WebTransaction(2L, 1L, new BigDecimal(1L), "RUR");

        TransferMoneyTask transferMoneyTask1 = new TransferMoneyTask(webTransaction1);
        TransferMoneyTask transferMoneyTask4 = new TransferMoneyTask(webTransaction4);
        for (int i = 1; i < threadsAmount; i++) {
            executor.execute(transferMoneyTask4);
            executor.execute(transferMoneyTask1);
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        WebAccount[] accounts = getWebAccountsGetRequest();
        System.out.println(Arrays.toString(accounts));
        assertEquals(accounts[0].getBalance(), new BigDecimal(100).setScale(2));
        assertEquals(accounts[1].getBalance(), new BigDecimal(200).setScale(2));
    }

    private class TransferMoneyTask implements Runnable {

        public TransferMoneyTask() {
        }

        public TransferMoneyTask(WebTransaction webTransaction) {
            this.webTransaction = webTransaction;
        }

        private WebTransaction webTransaction;

        @Override
        public void run() {
            HttpResponse response = null;
            try {
                response = transferMoneyWithNewClient(webTransaction);
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    System.out.println("_____________________---------NONONONONONON");
                    throw new RuntimeException("not 200 " + webTransaction);
                }
                WebAccount[] account = getWebAccountsWithNewClient();
                System.out.println("TRans " + webTransaction + "acc" + Arrays.toString(account));
            } catch (Exception e) {
                throw new RuntimeException("ddddddd 200", e);
            }
        }
    }

    private WebAccount[] getWebAccountsGetRequest() throws IOException, URISyntaxException {
        HttpResponse response = httpGet(GET_ACCOUNTS, commonClient);
        int statusCode = response.getStatusLine().getStatusCode();
        //assertEquals(200, statusCode);
        if (statusCode != 200) {
            System.out.println("_____________________---------getWebAccountsGetRequest");
            throw new RuntimeException("not 200");
        }
        return parseWebAccountsFromResponse(response);
    }

    private WebAccount[] getWebAccountsWithNewClient() throws IOException, URISyntaxException {
        HttpResponse response = httpGetWithNewClient(GET_ACCOUNTS);
        int statusCode = response.getStatusLine().getStatusCode();
        //assertEquals(200, statusCode);
        if (statusCode != 200) {
            System.out.println("_____________________---------getWebAccountsWithNewClient");
            throw new RuntimeException("not 200");
        }
        return parseWebAccountsFromResponse(response);
    }

    private HttpResponse transferMoneyPostRequest(WebTransaction webTransaction) throws IOException, URISyntaxException {
        return httpPost(TRANSFER, webTransaction, commonClient);
    }

    private HttpResponse transferMoneyWithNewClient(WebTransaction webTransaction) throws IOException, URISyntaxException {
        return httpPostWithNewClient(TRANSFER, webTransaction);
    }

    private WebAccount[] parseWebAccountsFromResponse(HttpResponse response) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        return parseEntityFromHttpResponse(response, WebAccount[].class);
    }
}
