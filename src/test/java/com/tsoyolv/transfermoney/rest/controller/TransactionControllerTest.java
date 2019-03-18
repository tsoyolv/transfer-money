package com.tsoyolv.transfermoney.rest.controller;

import com.tsoyolv.transfermoney.rest.RestPaths;
import com.tsoyolv.transfermoney.rest.webmodel.WebTransaction;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TransactionControllerTest extends AbstractControllerIntegrationTest {
    @Test
    public void testGetById() throws IOException, URISyntaxException {
        HttpResponse response = httpGet(RestPaths.REST_ROOT_PATH + RestPaths.TRANSACTION_ROOT_PATH + "/1", commonClient);
        assertNotNull(response);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        WebTransaction webTransaction = parseEntityFromHttpResponse(response, WebTransaction.class);
        assertNotNull(webTransaction);
        assertEquals(new BigDecimal(100).setScale(2), webTransaction.getAmount());
    }

}