package com.tsoyolv.transfermoney.dao.impl.jdbc;

import com.tsoyolv.transfermoney.dao.AccountDao;
import com.tsoyolv.transfermoney.dao.impl.jdbc.util.ResultSetToModelMapper;
import com.tsoyolv.transfermoney.database.DatabaseConnector;
import com.tsoyolv.transfermoney.entity.Account;
import com.tsoyolv.transfermoney.entity.Transaction;
import com.tsoyolv.transfermoney.log.LoggerWrapper;
import org.apache.commons.dbutils.DbUtils;

import javax.ws.rs.NotSupportedException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.tsoyolv.transfermoney.database.DatabaseConnector.rollbackTransaction;

public class JdbcAccountDao extends AbstractJdbcDao<Account> implements AccountDao {
    private static final LoggerWrapper log = LoggerWrapper.getLogger(ResultSetToModelMapper.class);
    private static final BigDecimal zeroAmount = new BigDecimal(0).setScale(2, RoundingMode.HALF_EVEN);

    private ResultSetToModelMapper<Account> resultSetToModelMapper = new ResultSetToModelMapper<>();

    private JdbcTransactionDao transactionDao = new JdbcTransactionDao();

    @Override
    public Account get(Long accountId) {
        try {
            return super.get(accountId, new Account());
        } catch (SQLException e) {
            log.warn("Cannot get account", e);
        }
        return null;
    }

    @Override
    public Account save(Account account) {
        try {
            return super.save(account);
        } catch (SQLException e) {
            log.error("Error Inserting Account  " + account);
        }
        return null;
    }

    @Override
    public List<Account> get() {
        List<Account> accounts = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnector.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(getSelectSqlForTable(Account.class.getSimpleName(), null));
            while (rs.next()) {
                accounts.add(resultSetToModelMapper.map(rs, new Account()));
            }
            return accounts;
        } catch (SQLException e) {
            log.warn("Cannot get account", e);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return null;
    }

    @Override
    public boolean transferMoney(Transaction transaction) {
        Connection conn = null;
        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);
            Account fromAccount = lockAccountById(transaction.getAccountFromId(), conn);
            Account toAccount = lockAccountById(transaction.getAccountToId(), conn);
            updateAccounts(conn, transaction, fromAccount, toAccount);
            transactionDao.save(transaction);
            conn.commit();
        } catch (SQLException se) {
            log.error("transferAccountBalance(): User Transaction Failed, rollback initiated for: " + transaction, se);
            rollbackTransaction(conn);
            return false;
        } catch (Exception e) {
            log.error("Transfer money failed", e);
            return false;
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return true;
    }

    @Override
    public Account delete(Account forDelete) {
        throw new NotSupportedException("not implemented yet");
    }

    private void updateAccounts(Connection conn, Transaction transaction, Account from, Account to) throws SQLException {
        PreparedStatement updateStmt = null;
        BigDecimal fromAccountLeftOver = checkAndGetFromAccountLeftover(transaction, from);
        try {
            updateStmt = conn.prepareStatement("UPDATE ACCOUNT SET BALANCE = ? WHERE ACCOUNTID = ?");
            updateStmt.setBigDecimal(1, fromAccountLeftOver);
            updateStmt.setLong(2, transaction.getAccountFromId());
            updateStmt.addBatch();
            updateStmt.setBigDecimal(1, to.getBalance().add(transaction.getAmount()));
            updateStmt.setLong(2, transaction.getAccountToId());
            updateStmt.addBatch();
            int[] rowsUpdated = updateStmt.executeBatch();
            if (rowsUpdated[0] + rowsUpdated[1] != 2) {
                throw new SQLException("Update failed");
            }
        } finally {
            DbUtils.closeQuietly(updateStmt);
        }
    }

    private BigDecimal checkAndGetFromAccountLeftover(Transaction transaction, Account from) {
        BigDecimal fromAccountLeftOver = from.getBalance().subtract(transaction.getAmount());
        if (fromAccountLeftOver.compareTo(zeroAmount) < 0) {
            throw new RuntimeException("not enough balance");
        }
        return fromAccountLeftOver;
    }

    private Account lockAccountById(Long id, Connection connection) throws SQLException {
        ResultSet rs = null;
        PreparedStatement lockStatement = null;
        try {
            lockStatement = connection.prepareStatement("SELECT * FROM ACCOUNT WHERE ACCOUNTID = ? FOR UPDATE");
            lockStatement.setLong(1, id);
            rs = lockStatement.executeQuery();
            if (rs.next()) {
                return resultSetToModelMapper.map(rs, new Account());
            } else {
                log.debug("lockAccountById failed!");
                throw new SQLException("lockAccountById failed!");
            }
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(lockStatement);
        }
    }
}
