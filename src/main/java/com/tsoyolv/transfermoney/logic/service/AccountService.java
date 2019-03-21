package com.tsoyolv.transfermoney.logic.service;

import com.tsoyolv.transfermoney.logic.entity.Account;

import java.util.Collection;

/**
 * This is second layer for application.
 * Best practice: 3 layers: DAO -> Service -> Controller
 */
public interface AccountService {

    Account createAccount(Account account);

    Account updateAccount(Account account);

    Account getAccount(Long id);

    Collection<Account> getAccounts();
}
