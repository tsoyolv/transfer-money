package com.tsoyolv.transfermoney.logic.dao.impl.jdbc.util;

import com.tsoyolv.transfermoney.logic.entity.DbEntity;
import com.tsoyolv.transfermoney.LoggerWrapper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.tsoyolv.transfermoney.LoggerWrapper.JDBC_LOGGER_NAME;

public class ResultSetToModelMapper<T extends DbEntity> {

    private static final LoggerWrapper log = LoggerWrapper.getLogger(JDBC_LOGGER_NAME);

    public T map(ResultSet resultSet, T dbEntity) throws SQLException {
        Field[] fields = dbEntity.getClass().getDeclaredFields();
        for (Field declaredField : fields) {
            setEntityFieldFromResultSet(resultSet, dbEntity, declaredField);
        }
        return dbEntity;
    }

    private void setEntityFieldFromResultSet(ResultSet resultSet, T dbEntity, Field declaredField) throws SQLException {
        boolean accessible = declaredField.canAccess(dbEntity);
        declaredField.setAccessible(true);
        String name = declaredField.getName();
        try {
            declaredField.set(dbEntity, getRsValue(name, declaredField.getType(), resultSet));
        } catch (IllegalAccessException e) {
            log.warn("Cannot map resultset to model", e);
        }
        declaredField.setAccessible(accessible);
    }

    private Object getRsValue(String fieldName, Class fieldType, ResultSet resultSet) throws SQLException {
        if (Long.class.equals(fieldType) || long.class.equals(fieldType)) {
            return resultSet.getLong(fieldName);
        } else if (Date.class.equals(fieldType)) {
            return resultSet.getDate(fieldName);
        } else if (BigDecimal.class.equals(fieldType)) {
            return resultSet.getBigDecimal(fieldName);
        } else if (Boolean.class.equals(fieldType)) {
            return resultSet.getBoolean(fieldName);
        } else if (String.class.equals(fieldType)) {
            return resultSet.getString(fieldName);
        } else {
            return null;
        }
    }
}
