package com.tsoyolv.transfermoney.rest.controller.jersey;

import com.tsoyolv.transfermoney.rest.RestPaths;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Path(RestPaths.TEST_THREAD_SAFE_CONTROLLER)
@Produces(MediaType.APPLICATION_JSON)
public class TestControllerThreadForEveryController {
    public static final List<String> THREAD_NAMES = Collections.synchronizedList(new ArrayList<>());

    public TestControllerThreadForEveryController() {
        System.out.println("Created new TestControllerThreadForEveryController with hash [" + hashCode() + "] from called thread with name: " + Thread.currentThread().getName()+ " and id: " + Thread.currentThread().getId());
        THREAD_NAMES.add(Thread.currentThread().getName());
    }

    @GET
    public Response get() {
        System.out.println("TestControllerThreadForEveryController get Thread: " + Thread.currentThread().getName());
        return Response.status(Response.Status.OK).build();
    }
}
