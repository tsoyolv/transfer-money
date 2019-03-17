package com.tsoyolv.transfermoney.dao;

import com.tsoyolv.transfermoney.model.Account;
import com.tsoyolv.transfermoney.model.Transaction;

public interface AccountDao extends Dao<Account> {

    boolean transferMoney(Transaction transaction);
}
