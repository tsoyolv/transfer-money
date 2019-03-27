package com.tsoyolv.transfermoney.rest.controller.spark.guice.testdata;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.tsoyolv.transfermoney.embeddedserver.EmbeddedServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestGuiceModule extends AbstractModule {

    public static final List<String> THREAD_NAMES = Collections.synchronizedList(new ArrayList<>());

    public TestGuiceModule() {
        THREAD_NAMES.add(Thread.currentThread().getName());
        System.out.println("Created new TestGuiceModule with hash [" + hashCode() + "] from called thread with name: " + Thread.currentThread().getName()+ " and id: " + Thread.currentThread().getId());
    }

    @Override
    protected void configure() {
        bind(TestSingletonController.class).asEagerSingleton();
        // todo non-singleton scope bind(TestNonSingletonController.class).in(Singleton.class);
        bind(EmbeddedServer.class).to(TestEmbeddedServer.class);
    }
}
