package com.tsoyolv.transfermoney.logic.dao;

import com.tsoyolv.transfermoney.logic.entity.Account;
import com.tsoyolv.transfermoney.logic.entity.Transaction;

public interface AccountDao extends Dao<Account> {

    boolean transferMoney(Transaction transaction);
}
