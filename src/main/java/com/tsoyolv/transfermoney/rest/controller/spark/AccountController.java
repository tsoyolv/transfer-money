package com.tsoyolv.transfermoney.rest.controller.spark;

import com.tsoyolv.transfermoney.logic.dao.AccountDao;
import com.tsoyolv.transfermoney.logic.dao.impl.jdbc.JdbcAccountDao;
import com.tsoyolv.transfermoney.logic.entity.Account;
import com.tsoyolv.transfermoney.rest.webmodel.WebAccount;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Collection;

public class AccountController {

    private static AccountDao accountDao = new JdbcAccountDao();

    private static MapperFacade mapperFacade = new DefaultMapperFactory.Builder().build().getMapperFacade();

    public static Route getAccounts = (Request request, Response response) -> {
        Collection<Account> accounts = accountDao.get();
        return mapperFacade.mapAsList(accounts, WebAccount.class);
    };
}
