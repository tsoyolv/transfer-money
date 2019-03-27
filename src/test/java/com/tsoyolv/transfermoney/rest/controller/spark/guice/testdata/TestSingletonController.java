package com.tsoyolv.transfermoney.rest.controller.spark.guice.testdata;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestSingletonController {

    public static final List<String> THREAD_NAMES = Collections.synchronizedList(new ArrayList<>());

    private String name;

    public TestSingletonController() {
        String name = Thread.currentThread().getName();
        this.name = name;
        System.out.println("Created new TestSingletonController with hash [" + hashCode() + "] from called thread with name: " + name + " and id: " + Thread.currentThread().getId());
        THREAD_NAMES.add(name);
    }

    public Route simpleGet = (Request request, Response response) -> {
        System.out.println("TestSingletonController.simpleGet with name: " + name + " and hash: " + hashCode() + " called from thread with name: " + Thread.currentThread().getName());
        return "Hi";
    };
}
