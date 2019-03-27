package com.tsoyolv.transfermoney.rest.controller.spark.guice.testdata;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestNonSingletonController {
    public static final List<String> THREAD_NAMES = Collections.synchronizedList(new ArrayList<>());

    private String name;

    public TestNonSingletonController() {
        String name = Thread.currentThread().getName();
        this.name = name;
        THREAD_NAMES.add(name);
        System.out.println("Created new TestNonSingletonController with hash [" + hashCode() + "] from called thread with name: " + name + " and id: " + Thread.currentThread().getId());
    }

    public Route simpleGet = (Request request, Response response) -> {
        System.out.println("TestNonSingletonController.simpleGet with name: " + name + " and hash: " + hashCode() + " called from thread with name: " + Thread.currentThread().getName());
        return "Hi";
    };
}
