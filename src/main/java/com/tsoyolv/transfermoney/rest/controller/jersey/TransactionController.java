package com.tsoyolv.transfermoney.rest.controller.jersey;

import com.tsoyolv.transfermoney.dao.TransactionDao;
import com.tsoyolv.transfermoney.dao.impl.jdbc.JdbcTransactionDao;
import com.tsoyolv.transfermoney.rest.RestPaths;
import com.tsoyolv.transfermoney.rest.webmodel.WebTransaction;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(RestPaths.TRANSACTION_ROOT_PATH)
@Produces(MediaType.APPLICATION_JSON)
public class TransactionController {

    private TransactionDao transactionDao = new JdbcTransactionDao();

    private MapperFacade mapperFacade = new DefaultMapperFactory.Builder().build().getMapperFacade();

    @GET
    @Path("/{transactionId}")
    public WebTransaction get(@PathParam("transactionId") long transactionId) {
        return mapperFacade.map(transactionDao.get(transactionId), WebTransaction.class);
    }
}