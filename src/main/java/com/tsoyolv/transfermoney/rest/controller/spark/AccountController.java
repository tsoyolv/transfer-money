package com.tsoyolv.transfermoney.rest.controller.spark;

import com.tsoyolv.transfermoney.logic.entity.Account;
import com.tsoyolv.transfermoney.logic.service.AccountService;
import com.tsoyolv.transfermoney.logic.service.impl.AccountServiceImpl;
import com.tsoyolv.transfermoney.rest.webmodel.WebAccount;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Collection;
import java.util.List;

import static com.tsoyolv.transfermoney.embeddedserver.impl.spark.SparkEmbeddedServer.ID;
import static com.tsoyolv.transfermoney.rest.controller.spark.serialization.SerializationUtils.deserializeObject;
import static com.tsoyolv.transfermoney.rest.controller.spark.serialization.SerializationUtils.serializeObject;

public class AccountController {

    private static AccountService accountService = new AccountServiceImpl();

    private static MapperFacade mapperFacade = new DefaultMapperFactory.Builder().build().getMapperFacade();

    public static Route getAccounts = (Request request, Response response) -> {
        Collection<Account> accounts = accountService.getAccounts();
        List<WebAccount> webAccounts = mapperFacade.mapAsList(accounts, WebAccount.class);
        return serializeObject(webAccounts);
    };

    public static Route getAccount = (Request request, Response response) -> {
        response.type("application/json");
        String id = request.params(ID);
        Account account = accountService.getAccount(Long.valueOf(id));
        WebAccount webAccount = mapperFacade.map(account, WebAccount.class);
        return serializeObject(webAccount);
    };

    public static Route createAccount = (Request request, Response response) -> {
        response.type("application/json");
        String body = request.body();
        WebAccount webAccount = deserializeObject(body, WebAccount.class);
        Account account = mapperFacade.map(webAccount, Account.class);
        Account createdAccount = accountService.createAccount(account);
        WebAccount createdWebAccount = mapperFacade.map(createdAccount, WebAccount.class);
        return serializeObject(createdWebAccount);
    };
}
