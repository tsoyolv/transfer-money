package com.tsoyolv.transfermoney.logic.dao.impl.jooq;

import com.google.inject.Inject;
import com.tsoyolv.transfermoney.LoggerWrapper;
import com.tsoyolv.transfermoney.logic.dao.AccountDao;
import com.tsoyolv.transfermoney.logic.entity.Account;
import com.tsoyolv.transfermoney.logic.entity.Transaction;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.SelectQuery;

import javax.ws.rs.NotSupportedException;
import java.math.BigInteger;
import java.util.Collection;

import static com.tsoyolv.transfermoney.LoggerWrapper.JDBC_LOGGER_NAME;
import static com.tsoyolv.transfermoney.generated.jooq.tables.Account.ACCOUNT;

public class JooqAccountDao implements AccountDao {

    private static final LoggerWrapper log = LoggerWrapper.getLogger(JDBC_LOGGER_NAME);

    @Inject
    private DSLContext dsl;

    @Override
    public boolean transferMoney(Transaction transaction) {
        return false;
    }

    @Override
    public Collection<Account> get() {
        try {
            SelectQuery<Record> select = dsl.selectQuery();
            select.addSelect(ACCOUNT.ACCOUNTID,
                    ACCOUNT.ACCOUNTNUMBER,
                    ACCOUNT.BALANCE,
                    ACCOUNT.CURRENCYCODE);
            select.addFrom(ACCOUNT);
            if (log.isDebugEnabled()) {
                log.debug("Jooq generates sql statement: {}", select.getSQL());
            }
            return select.fetch(new AccountMapper());
        } catch (Exception e) {
            log.error("Accounts", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account get(Long id) {
        SelectQuery<Record> select = dsl.selectQuery();
        select.addSelect(ACCOUNT.ACCOUNTID,
                ACCOUNT.ACCOUNTNUMBER,
                ACCOUNT.BALANCE,
                ACCOUNT.CURRENCYCODE);
        select.addFrom(ACCOUNT);
        select.addConditions(ACCOUNT.ACCOUNTID.eq(BigInteger.valueOf(id)));
        if (log.isDebugEnabled()) {
            log.debug("Jooq generates sql statement: {}", select.getSQL());
        }
        return select.fetchOne(new AccountMapper());
    }

    @Override
    public Account save(Account forSave) {
        throw new NotSupportedException("not implemnted yet");
    }

    @Override
    public Account delete(Account forDelete) {
        throw new NotSupportedException("not implemnted yet");
    }

    private class AccountMapper implements RecordMapper<Record, Account> {

        @Override
        public Account map(Record record) {
            Account account = new Account();
            account.setAccountId(record.get(ACCOUNT.ACCOUNTID).longValue());
            account.setAccountNumber(record.get(ACCOUNT.ACCOUNTNUMBER));
            account.setBalance(record.get(ACCOUNT.BALANCE));
            return account;
        }
    }
}
