package com.tsoyolv.transfermoney.dao.impl.jdbc;

import com.tsoyolv.transfermoney.dao.impl.jdbc.util.PreparedStatementForSaveCreator;
import com.tsoyolv.transfermoney.dao.impl.jdbc.util.ResultSetToModelMapper;
import com.tsoyolv.transfermoney.database.DatabaseConnector;
import com.tsoyolv.transfermoney.entity.DbEntity;
import com.tsoyolv.transfermoney.entity.annotation.Id;
import com.tsoyolv.transfermoney.log.LoggerWrapper;
import org.apache.commons.dbutils.DbUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.tsoyolv.transfermoney.database.DatabaseConnector.rollbackTransaction;
import static com.tsoyolv.transfermoney.log.LoggerWrapper.JDBC_LOGGER_NAME;

public abstract class AbstractJdbcDao<T extends DbEntity> {
    private static final LoggerWrapper log = LoggerWrapper.getLogger(JDBC_LOGGER_NAME);

    private static final String SELECT = "SELECT ";
    private static final String ASTERISK = "*";
    private static final String FROM = " FROM ";
    private static final String SELECT_FROM = SELECT + ASTERISK + FROM;
    private static final String WHERE = " WHERE ";

    private ResultSetToModelMapper<T> resultSetToModelMapper = new ResultSetToModelMapper<>();
    private PreparedStatementForSaveCreator<T> updateCreator = new PreparedStatementForSaveCreator<>();

    public T get(Long id, T t) throws SQLException {
        if (t == null) {
            return null;
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnector.getConnection();
            String idFieldName = getEntityIdFieldName(t);
            String entityName = t.getClass().getSimpleName();
            String sql = SELECT_FROM + entityName + WHERE + idFieldName + " = ?";
            if (log.isDebugEnabled()) {
                log.debug("Sql for entity {} : {}", entityName, sql);
            }
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return resultSetToModelMapper.map(rs, t);
            }
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return null;
    }

    public T save(T forSave) throws SQLException {
        Connection connection = null;
        PreparedStatement statementForSaveEntity = null;
        ResultSet generatedKeys = null;
        try {
            connection = DatabaseConnector.getConnection();
            connection.setAutoCommit(false);
            statementForSaveEntity = updateCreator.createPreparedStatementForSave(forSave, connection);
            int affectedRows = statementForSaveEntity.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Creating " + forSave.getClass().getSimpleName() + " failed, no rows affected.");
            }
            generatedKeys = statementForSaveEntity.getGeneratedKeys();
            if (generatedKeys.next()) {
                setEntityIdField(forSave, generatedKeys.getLong(1));
            } else {
                throw new SQLException("Creating " + forSave.getClass().getSimpleName() + " failed, no ID obtained.");
            }
            connection.commit();
            return forSave;
        } catch (SQLException se) {
            rollbackTransaction(connection);
            throw se;
        } finally {
            DbUtils.closeQuietly(connection, statementForSaveEntity, generatedKeys);
        }
    }

    protected String getSelectSqlForTable(String tableName, List<String> fields) {
        StringBuilder sql = new StringBuilder(SELECT);
        if (fields == null || fields.isEmpty()) {
            sql.append(ASTERISK);
        } else {
            fields.forEach(field -> {
                sql.append(field).append(",");
            });
            sql.deleteCharAt(sql.length() - 1);
        }
        sql.append(FROM).append(tableName);
        if (log.isDebugEnabled()) {
            log.debug("Select for table {} : {}", tableName, sql.toString());
        }
        return sql.toString();
    }

    private String getEntityIdFieldName(T entity) {
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Id.class)) {
                return field.getName();
            }
        }
        return null;
    }

    private void setEntityIdField(T entity, Long id) {
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Id.class)) {
                boolean accessible = field.canAccess(entity);
                field.setAccessible(true);
                try {
                    field.set(entity, id);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                field.setAccessible(accessible);
                break;
            }
        }
    }
}
