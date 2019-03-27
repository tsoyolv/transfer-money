package com.tsoyolv.transfermoney.rest.controller.spark.guice.testdata;

import com.google.inject.Inject;
import com.tsoyolv.transfermoney.embeddedserver.AbstractEmbeddedServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.stop;

public class TestEmbeddedServer extends AbstractEmbeddedServer {

    public static final List<String> THREAD_NAMES = Collections.synchronizedList(new ArrayList<>());

    private String name;

    public TestEmbeddedServer() {
        String name = Thread.currentThread().getName();
        this.name = name;
        THREAD_NAMES.add(name);
        System.out.println("Created new TestEmbeddedServer with hash [" + hashCode() + "] from called thread with name: " + name + " and id: " + Thread.currentThread().getId());
    }

    @Inject
    private TestSingletonController testSingletonController;

    //@Inject
    //private TestNonSingletonController testNonSingletonController;

    @Override
    public void startServer() throws Exception {
        System.out.println("TestEmbeddedServer.startServer with name: " + name + " and hash: " + hashCode() + " called from thread with name: " + Thread.currentThread().getName());
        port(getServerPort());
        get("/hello", testSingletonController.simpleGet);
        //get( "/hello2", testNonSingletonController.simpleGet);
    }

    @Override
    public void stopServer() throws Exception {
        stop();
    }
}
