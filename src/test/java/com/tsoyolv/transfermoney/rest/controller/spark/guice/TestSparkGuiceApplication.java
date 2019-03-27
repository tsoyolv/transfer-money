package com.tsoyolv.transfermoney.rest.controller.spark.guice;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.tsoyolv.transfermoney.embeddedserver.EmbeddedServer;
import com.tsoyolv.transfermoney.rest.controller.AbstractControllerIntegrationTest;
import com.tsoyolv.transfermoney.rest.controller.spark.guice.testdata.TestEmbeddedServer;
import com.tsoyolv.transfermoney.rest.controller.spark.guice.testdata.TestGuiceModule;
import com.tsoyolv.transfermoney.rest.controller.spark.guice.testdata.TestSingletonController;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class TestSparkGuiceApplication extends AbstractControllerIntegrationTest {

    @Override
    protected EmbeddedServer initEmbeddedServer() {
        Injector injector = Guice.createInjector(new TestGuiceModule());
        return injector.getInstance(EmbeddedServer.class);
    }

    @Test
    public void testSparkInstances() {
    }

    @Test
    public void testSingleton() throws InterruptedException {
        int threadsAmount = 100;
        runMultithreadingTest(() -> {
            try {
                HttpResponse httpResponse = httpGet("/hello", commonClient);
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
        }, threadsAmount);
        assertEquals(1, TestSingletonController.THREAD_NAMES.size());
        assertEquals(1, TestGuiceModule.THREAD_NAMES.size());
        assertEquals(1, TestEmbeddedServer.THREAD_NAMES.size());
        synchronized(TestSingletonController.THREAD_NAMES) {
            Iterator<String> iterator = TestSingletonController.THREAD_NAMES.iterator();
            System.out.println("TestSingletonController.THREAD_NAMES");
            while (iterator.hasNext()) {
                System.out.print(iterator.next() + " || ");
            }
        }
        System.out.println();
        synchronized(TestGuiceModule.THREAD_NAMES) {
            Iterator<String> iterator = TestGuiceModule.THREAD_NAMES.iterator();
            System.out.println("TestGuiceModule.THREAD_NAMES");
            while (iterator.hasNext()) {
                System.out.print(iterator.next() + " || ");
            }
        }
        System.out.println();
        synchronized(TestEmbeddedServer.THREAD_NAMES) {
            Iterator<String> iterator = TestEmbeddedServer.THREAD_NAMES.iterator();
            System.out.println("TestEmbeddedServer.THREAD_NAMES");
            while (iterator.hasNext()) {
                System.out.print(iterator.next() + " || ");
            }
        }
    }

    @Test
    public void testNonSingleton() throws InterruptedException {

    }


    private void runMultithreadingTest(Runnable runnable, int threadsAmount) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threadsAmount);
        for (int i = 0; i < threadsAmount; i++) {
            executor.execute(runnable);
        }
        executor.shutdown();
        System.out.println("Shutdown");
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }
}
