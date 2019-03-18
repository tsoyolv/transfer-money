package com.tsoyolv.transfermoney.rest.controller;

import com.tsoyolv.transfermoney.rest.RestPaths;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Path(RestPaths.TEST_THREAD_SAFE_CONTROLLER)
@Produces(MediaType.APPLICATION_JSON)
public class TestControllerThreadForEveryController {
    public static final List<String> THREAD_NAMES = Collections.synchronizedList(new ArrayList<>());

    private String threadName;

    public TestControllerThreadForEveryController() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        threadName = generatedString;
    }

    @GET
    public Response get() {
        THREAD_NAMES.add(threadName);
        System.out.println("TestControllerThreadForEveryController get Thread: " + Thread.currentThread().getName() + ". This: " + threadName);
        return Response.status(Response.Status.OK).build();
    }
}
