package com.tsoyolv.transfermoney.rest.controller.spark;

import com.tsoyolv.transfermoney.dao.AccountDao;
import com.tsoyolv.transfermoney.dao.impl.jdbc.JdbcAccountDao;
import spark.Request;
import spark.Response;
import spark.Route;

public class AccountController {

    public static AccountDao accountDao = new JdbcAccountDao();

    public static Route getAccounts = (Request request, Response response) -> accountDao.get();
}
