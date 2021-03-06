package com.tsoyolv.transfermoney.rest.controller.jersey;

import com.tsoyolv.transfermoney.rest.RestPaths;
import com.tsoyolv.transfermoney.rest.controller.AbstractControllerIntegrationTest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This Test shows that embedded Jetty + Jersey Servlet creates new Controller (Service with @Path) for every HTTP-request.
 * It means that every request potentially Thread-safe.
 */
public class ThreadForEveryControllerTest extends AbstractControllerIntegrationTest {

    @Test
    public void get() throws InterruptedException {
        int threadsAmount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadsAmount);
        for (int i = 0; i < threadsAmount; i++) {
            executor.execute(new TestTask());
        }
        executor.shutdown();
        System.out.println("Shutdown");
        executor.awaitTermination(1, TimeUnit.MINUTES);

        synchronized(TestControllerThreadForEveryController.THREAD_NAMES) {
            Set<String> stringSet = new HashSet<>(TestControllerThreadForEveryController.THREAD_NAMES);
            assertNotNull(TestControllerThreadForEveryController.THREAD_NAMES);
            assertEquals(TestControllerThreadForEveryController.THREAD_NAMES.size(), stringSet.size());
           // assertEquals(threadsAmount, stringSet.size());
            System.out.println(TestControllerThreadForEveryController.THREAD_NAMES.size());
            Iterator<String> iterator = TestControllerThreadForEveryController.THREAD_NAMES.iterator();
            while (iterator.hasNext()) {
                System.out.print(iterator.next() + " || ");
            }
        }
    }

    private class TestTask implements Runnable {

        @Override
        public void run() {
            try {
                HttpResponse httpResponse = httpGet(RestPaths.REST_ROOT_PATH + RestPaths.TEST_THREAD_SAFE_CONTROLLER, commonClient);
                if (HttpStatus.SC_OK != httpResponse.getStatusLine().getStatusCode()) {
                    System.out.println("FAIL");
                    throw new RuntimeException("FAIL");
                }
                System.out.println("OK");
            } catch (URISyntaxException e) {
                System.out.println("URISyntaxException" + e.getMessage());
                throw new RuntimeException(e);
            } catch (IOException e) {
                System.out.println("IOException" + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}