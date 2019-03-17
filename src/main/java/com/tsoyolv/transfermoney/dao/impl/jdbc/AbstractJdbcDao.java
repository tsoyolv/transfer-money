package com.tsoyolv.transfermoney.dao.impl.jdbc;

import com.tsoyolv.transfermoney.dao.impl.jdbc.util.PreparedStatementForSaveCreator;
import com.tsoyolv.transfermoney.dao.impl.jdbc.util.ResultSetToModelMapper;
import com.tsoyolv.transfermoney.database.DatabaseConnector;
import com.tsoyolv.transfermoney.model.DbEntity;
import com.tsoyolv.transfermoney.model.annotation.Id;
import org.apache.commons.dbutils.DbUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.tsoyolv.transfermoney.database.DatabaseConnector.rollbackTransaction;

public abstract class AbstractJdbcDao<T extends DbEntity> {

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
            String idFieldName = getIdFieldName(t);
            stmt = conn.prepareStatement("SELECT * FROM " + t.getClass().getSimpleName() + " WHERE " + idFieldName + " = ?");
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

    private String getIdFieldName(T entity) {
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Id.class)) {
                return field.getName();
            }
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

    private void setEntityIdField(T entity, Long id) {
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Id.class)) {
                boolean accessible = field.isAccessible();
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
