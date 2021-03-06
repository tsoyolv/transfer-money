package com.tsoyolv.transfermoney.logic.service.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.tsoyolv.transfermoney.logic.dao.AccountDao;
import com.tsoyolv.transfermoney.logic.entity.Account;
import com.tsoyolv.transfermoney.logic.service.AccountService;

import java.util.Collection;

public class AccountServiceImpl implements AccountService {

    @Inject @Named("jooqAccountDao")
    private AccountDao accountDao;

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
