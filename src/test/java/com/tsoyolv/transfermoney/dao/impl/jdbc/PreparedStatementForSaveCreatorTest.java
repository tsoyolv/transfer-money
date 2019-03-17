package com.tsoyolv.transfermoney.dao.impl.jdbc;

import com.tsoyolv.transfermoney.model.Account;
import org.junit.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PreparedStatementForSaveCreatorTest {

    @Test
    public void testCreateInsertQueryForEntity() {
        Account account = new Account(501L, "4789561237894561", new BigDecimal(500000));
        PreparedStatementForSaveCreator<Account> preparedStatementForSaveCreator = new PreparedStatementForSaveCreator<>();
        Map<Integer, Field> preparedStatementParameters = preparedStatementForSaveCreator.createPreparedStatementParameters(account);
        String insertQueryForEntity = preparedStatementForSaveCreator.createInsertQueryForEntity(preparedStatementParameters, account);
        assertEquals("INSERT INTO Account (accountNumber,balance) VALUES (?,?)", insertQueryForEntity);
    }
}