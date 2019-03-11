package com.tsoyolv.transfermoney.dao;

import com.tsoyolv.transfermoney.model.Account;
import com.tsoyolv.transfermoney.model.Transaction;

import java.util.List;

public interface AccountDao {
    Account get(Long id);

    Account save(Account account);

    List<Account> get();

    boolean transferMoney(Transaction transaction);
}
