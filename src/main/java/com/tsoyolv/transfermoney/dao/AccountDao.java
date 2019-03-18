package com.tsoyolv.transfermoney.dao;

import com.tsoyolv.transfermoney.entity.Account;
import com.tsoyolv.transfermoney.entity.Transaction;

public interface AccountDao extends Dao<Account> {

    boolean transferMoney(Transaction transaction);
}
