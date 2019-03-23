package com.tsoyolv.transfermoney.logic.service.impl;

import com.tsoyolv.transfermoney.logic.dao.AccountDao;
import com.tsoyolv.transfermoney.logic.dao.impl.jdbc.JdbcAccountDao;
import com.tsoyolv.transfermoney.logic.dao.impl.jooq.JooqAccountDao;
import com.tsoyolv.transfermoney.logic.entity.Account;
import com.tsoyolv.transfermoney.logic.service.AccountService;

import java.util.Collection;

public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao = new JooqAccountDao();//JdbcAccountDao();

    @Override
    public Account createAccount(Account account) {
        return accountDao.save(account);
    }

    @Override
    public Account updateAccount(Account account) {
        return accountDao.save(account);
    }

    @Override
    public Account getAccount(Long id) {
        return accountDao.get(id);
    }

    @Override
    public Collection<Account> getAccounts() {
        return accountDao.get();
    }
}
