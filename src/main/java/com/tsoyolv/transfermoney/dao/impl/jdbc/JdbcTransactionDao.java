package com.tsoyolv.transfermoney.dao.impl.jdbc;

import com.tsoyolv.transfermoney.dao.TransactionDao;
import com.tsoyolv.transfermoney.entity.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.NotSupportedException;
import java.sql.SQLException;
import java.util.Collection;

public class JdbcTransactionDao extends AbstractJdbcDao<Transaction> implements TransactionDao {

    private static final Logger log = LogManager.getLogger(JdbcTransactionDao.class);

    @Override
    public Transaction get(Long id) {
        try {
            return super.get(id, new Transaction());
        } catch (SQLException e) {
            log.warn("Cannot get Transaction", e);
        }
        return null;
    }

    @Override
    public Transaction save(Transaction transaction) {
        try {
            return super.save(transaction);
        } catch (SQLException e) {
            log.error("Error Inserting Transaction  " + transaction);
        }
        return null;
    }

    @Override
    public Collection<Transaction> get() {
        throw new NotSupportedException("not implemented yet");
    }

    @Override
    public Transaction delete(Transaction forDelete) {
        throw new NotSupportedException("not implemented yet");
    }
}
