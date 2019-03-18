package com.tsoyolv.transfermoney;

import com.tsoyolv.transfermoney.entity.Account;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SharedObjectInMultipleThreadsTest {

    @Test
    public void testMultipleThreads() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        Account account = new Account(1L, "", new BigDecimal(100));
        for (int i = 0; i < 10; i++) {
            executor.execute(new Task(account));
        }
        executor.shutdown();
        System.out.println("Shutdown");
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }

    private class Task implements Runnable {

        private Account account;

        public Task(Account account) {
            this.account = account;
        }

        @Override
        public void run() {
            System.out.println(System.identityHashCode(account));
        }
    }
}
