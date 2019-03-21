package com.tsoyolv.transfermoney.logic.dao.impl.jdbc.util;

import com.tsoyolv.transfermoney.logic.entity.DbEntity;
import com.tsoyolv.transfermoney.logic.entity.annotation.Id;
import com.tsoyolv.transfermoney.LoggerWrapper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.tsoyolv.transfermoney.LoggerWrapper.JDBC_LOGGER_NAME;

public class PreparedStatementForSaveCreator<T extends DbEntity> {

    private static final LoggerWrapper log = LoggerWrapper.getLogger(JDBC_LOGGER_NAME);
    private static final String INSERT_INTO = "INSERT INTO ";
    private static final String VALUES = " VALUES (";

    public PreparedStatement createPreparedStatementForSave(T entity, Connection connection) throws SQLException {
        Map<Integer, Field> preparedStatementParameters = createPreparedStatementParameters(entity);
        String insertQueryForEntity = createInsertQueryForEntity(preparedStatementParameters, entity);
        if (log.isDebugEnabled()) {
            log.debug("Statement for entity " + entity.getClass().getSimpleName() + ": " + insertQueryForEntity);
        }
        PreparedStatement statementForSave = connection.prepareStatement(insertQueryForEntity);
        for (Map.Entry<Integer, Field> entry : preparedStatementParameters.entrySet()) {
            int indexForPreparedStatement = entry.getKey();
            Field field = entry.getValue();
            setPreparedStatementParameter(entity, statementForSave, indexForPreparedStatement, field);
        }
        return statementForSave;
    }

    public String createInsertQueryForEntity(Map<Integer, Field> parameters, T entity) {
        if (entity == null) {
            return null;
        }
        if (parameters == null || parameters.isEmpty()) {
            parameters = createPreparedStatementParameters(entity);
        }
        StringBuilder questionMarks = new StringBuilder(VALUES);
        StringBuilder insertQuery = new StringBuilder(INSERT_INTO).append(getTableName(entity)).append(" (");
        for (Map.Entry<Integer, Field> entry : parameters.entrySet()) {
            insertQuery.append(entry.getValue().getName()).append(",");
            questionMarks.append("?").append(",");
        }
        insertQuery.deleteCharAt(insertQuery.length() - 1).append(")");
        questionMarks.deleteCharAt(questionMarks.length() - 1).append(")");
        return insertQuery.append(questionMarks).toString();
    }

    private Map<Integer, Field> createPreparedStatementParameters(T entity) {
        if (entity == null) {
            return null;
        }
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        Map<Integer, Field> indexFieldNameMap = new HashMap<>(declaredFields.length);
        int indexForPreparedStatement = 1;
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Id.class)) {
                continue;
            }
            indexFieldNameMap.put(indexForPreparedStatement++, field);
        }
        return indexFieldNameMap;
    }

    private String getTableName(T entity) {
        return entity.getClass().getSimpleName();
    }

    private void setPreparedStatementParameter(T entity, PreparedStatement stmt, int index, Field field) throws SQLException {
        boolean accessible = field.canAccess(entity);
        field.setAccessible(true);
        if (Long.class.equals(field.getType())) {
            Object value = getFieldValue(entity, field);
            stmt.setLong(index, (Long) value);
        } else if (Integer.class.equals(field.getType())) {
            Object value = getFieldValue(entity, field);
            stmt.setInt(index, (Integer) value);
        } else if (BigDecimal.class.equals(field.getType())) {
            Object value = getFieldValue(entity, field);
            stmt.setBigDecimal(index, (BigDecimal) value);
        } else if (String.class.equals(field.getType())) {
            Object value = getFieldValue(entity, field);
            stmt.setString(index, (String) value);
        }
        field.setAccessible(accessible);
    }

    private Object getFieldValue(T entity, Field field) {
        Object value = null;
        try {
            value = field.get(entity);
        } catch (IllegalAccessException e) {
            log.warn("Cannot map resultset to model", e);
        }
        return value;
    }
}
