package com.tsoyolv.transfermoney.logic.dao.impl.jooq;

import com.tsoyolv.transfermoney.LoggerWrapper;
import com.tsoyolv.transfermoney.database.DatabaseConnector;
import com.tsoyolv.transfermoney.logic.dao.AccountDao;
import com.tsoyolv.transfermoney.logic.entity.Account;
import com.tsoyolv.transfermoney.logic.entity.Transaction;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record4;
import org.jooq.RecordMapper;
import org.jooq.SQLDialect;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DSL;

import javax.ws.rs.NotSupportedException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import static com.tsoyolv.transfermoney.LoggerWrapper.JDBC_LOGGER_NAME;
import static com.tsoyolv.transfermoney.generated.jooq.tables.Account.ACCOUNT;

public class JooqAccountDao implements AccountDao {

    private static final LoggerWrapper log = LoggerWrapper.getLogger(JDBC_LOGGER_NAME);

    @Override
    public boolean transferMoney(Transaction transaction) {
        return false;
    }

    @Override
    public Collection<Account> get() {
        try (Connection conn = DatabaseConnector.getConnection()) {
            DSLContext dsl = DSL.using(conn, SQLDialect.H2);
            SelectJoinStep<Record4<BigInteger, String, BigDecimal, String>> selectStatement = dsl.
                    select(ACCOUNT.ACCOUNTID,
                            ACCOUNT.ACCOUNTNUMBER,
                            ACCOUNT.BALANCE,
                            ACCOUNT.CURRENCYCODE).
                    from(ACCOUNT);
            if (log.isDebugEnabled()) {
                log.debug("Jooq generates sql statement: {}", selectStatement.getSQL());
            }
            return selectStatement.fetch(new AccountMapper());
        } catch (SQLException e) {
            log.error("Accounts", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account get(Long id) {
        throw new NotSupportedException("not implemnted yet");
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
