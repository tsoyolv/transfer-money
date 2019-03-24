package com.tsoyolv.transfermoney.rest.controller.spark;

import com.google.inject.Inject;
import com.tsoyolv.transfermoney.logic.entity.Account;
import com.tsoyolv.transfermoney.logic.service.AccountService;
import com.tsoyolv.transfermoney.rest.webmodel.WebAccount;
import ma.glasnost.orika.MapperFacade;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Collection;
import java.util.List;

import static com.tsoyolv.transfermoney.embeddedserver.impl.spark.SparkEmbeddedServer.ID;
import static com.tsoyolv.transfermoney.rest.controller.spark.serialization.SerializationUtils.deserializeObject;
import static com.tsoyolv.transfermoney.rest.controller.spark.serialization.SerializationUtils.serializeObject;

public class AccountController {

    @Inject
    private AccountService accountService;

    @Inject
    private MapperFacade mapperFacade;

    public Route getAccounts = (Request request, Response response) -> {
        Collection<Account> accounts = accountService.getAccounts();
        List<WebAccount> webAccounts = mapperFacade.mapAsList(accounts, WebAccount.class);
        return serializeObject(webAccounts);
    };

    public Route getAccount = (Request request, Response response) -> {
        response.type("application/json");
        String id = request.params(ID);
        Account account = accountService.getAccount(Long.valueOf(id));
        WebAccount webAccount = mapperFacade.map(account, WebAccount.class);
        return serializeObject(webAccount);
    };

    public Route createAccount = (Request request, Response response) -> {
        response.type("application/json");
        String body = request.body();
        WebAccount webAccount = deserializeObject(body, WebAccount.class);
        Account account = mapperFacade.map(webAccount, Account.class);
        Account createdAccount = accountService.createAccount(account);
        WebAccount createdWebAccount = mapperFacade.map(createdAccount, WebAccount.class);
        return serializeObject(createdWebAccount);
    };
}
