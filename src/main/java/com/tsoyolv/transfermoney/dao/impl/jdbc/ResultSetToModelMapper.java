package com.tsoyolv.transfermoney.dao.impl.jdbc;

import com.tsoyolv.transfermoney.model.DbEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetToModelMapper<T extends DbEntity> {

    private static final Logger log = LogManager.getLogger(ResultSetToModelMapper.class);

    public T map(ResultSet resultSet, T dbEntity) throws SQLException {
        Field[] fields = dbEntity.getClass().getDeclaredFields();
        for (Field declaredField : fields) {
            setEntityFieldFromResultSet(resultSet, dbEntity, declaredField);
        }
        return dbEntity;
    }

    private void setEntityFieldFromResultSet(ResultSet resultSet, T dbEntity, Field declaredField) throws SQLException {
        boolean accessible = declaredField.isAccessible();
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
        if (Long.class.equals(fieldType)) {
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
