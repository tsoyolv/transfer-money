package com.tsoyolv.transfermoney.rest.controller;

import com.tsoyolv.transfermoney.UriPath;
import com.tsoyolv.transfermoney.rest.webmodel.WebAccount;
import com.tsoyolv.transfermoney.rest.webmodel.WebTransaction;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;


public class AccountControllerTest extends AbstractControllerTest {
    private static final String GET_ACCOUNTS = UriPath.REST_ROOT_PATH + UriPath.ACCOUNT_ROOT_PATH;
    private static final String TRANSFER = UriPath.REST_ROOT_PATH + UriPath.ACCOUNT_ROOT_PATH + UriPath.TRANSFER_BETWEEN_ACCOUNTS_PATH;

    @Test
    public void testSimpleTransferringBetweenAccount() throws IOException, URISyntaxException, InterruptedException {
        WebTransaction webTransaction1 = new WebTransaction(1L, 2L, new BigDecimal(12L), "RUR"); // 88 212
        WebTransaction webTransaction2 = new WebTransaction(3L, 2L, new BigDecimal(21L), "RUR"); // 479  221|233
        WebTransaction webTransaction3 = new WebTransaction(1L, 4L, new BigDecimal(44L), "RUR"); // 44|66  544

        HttpResponse response = transferMoneyPostRequest(webTransaction1);
        HttpResponse response1 = transferMoneyPostRequest(webTransaction2);
        HttpResponse response2 = transferMoneyPostRequest(webTransaction3);

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
        String jsonString = EntityUtils.toString(response.getEntity());
        return mapper.readValue(jsonString, WebAccount[].class);
    }
}
