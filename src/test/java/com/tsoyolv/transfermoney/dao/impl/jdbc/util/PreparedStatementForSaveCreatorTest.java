package com.tsoyolv.transfermoney.dao.impl.jdbc.util;

import com.tsoyolv.transfermoney.entity.Account;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class PreparedStatementForSaveCreatorTest {

    @Test
    public void testCreateInsertQueryForEntity() {
        Account account = new Account(501L, "4789561237894561", new BigDecimal(500000));
        PreparedStatementForSaveCreator<Account> preparedStatementForSaveCreator = new PreparedStatementForSaveCreator<>();
        String insertQueryForEntity = preparedStatementForSaveCreator.createInsertQueryForEntity(null, account);
        assertEquals("INSERT INTO Account (accountNumber,balance) VALUES (?,?)", insertQueryForEntity);
    }
}