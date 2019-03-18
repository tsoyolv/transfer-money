package com.tsoyolv.transfermoney.rest.controller;

import com.tsoyolv.transfermoney.dao.AccountDao;
import com.tsoyolv.transfermoney.dao.impl.jdbc.JdbcAccountDao;
import com.tsoyolv.transfermoney.entity.Account;
import com.tsoyolv.transfermoney.entity.Transaction;
import com.tsoyolv.transfermoney.rest.RestPaths;
import com.tsoyolv.transfermoney.rest.webmodel.WebAccount;
import com.tsoyolv.transfermoney.rest.webmodel.WebTransaction;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Path(RestPaths.ACCOUNT_ROOT_PATH)
@Produces(MediaType.APPLICATION_JSON)
public class AccountController {

    private AccountDao accountDao = new JdbcAccountDao();

    private MapperFacade mapperFacade = new DefaultMapperFactory.Builder().build().getMapperFacade();

    @GET
    public Collection<WebAccount> get() {
        Collection<Account> accounts = accountDao.get();
        return mapperFacade.mapAsList(accounts, WebAccount.class);
    }

    @GET
    @Path("/{accountId}")
    public WebAccount get(@PathParam("accountId") long accountId) {
        Account account = accountDao.get(accountId);
        return mapperFacade.map(account, WebAccount.class);
    }

    @POST
    public WebAccount create(WebAccount account) {
        Account accountForUpdate = mapperFacade.map(account, Account.class);
        Account save = accountDao.save(accountForUpdate);
        return mapperFacade.map(save, WebAccount.class);
    }

    @POST
    @Path(RestPaths.TRANSFER_BETWEEN_ACCOUNTS_PATH)
    public Response transfer(WebTransaction webTransaction) {
        Transaction transaction = mapperFacade.map(webTransaction, Transaction.class);
        boolean success = accountDao.transferMoney(transaction);
        if (success) {
            return Response.status(Response.Status.OK).build();
        } else {
            throw new WebApplicationException("Transfer failed", Response.Status.BAD_REQUEST);
        }
    }
}