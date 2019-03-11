package com.tsoyolv.transfermoney.dao.impl.jdbc;

import com.tsoyolv.transfermoney.model.DbEntity;
import com.tsoyolv.transfermoney.model.annotation.Id;
import org.apache.commons.dbutils.DbUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PreparedStatementForUpdateCreator<T extends DbEntity> {

    private static final Logger log = LogManager.getLogger(PreparedStatementForUpdateCreator.class);

    public PreparedStatement createPreparedStatementForUpdate(T entity, Connection connection) throws SQLException {
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        Map<Integer, Field> indexFieldNameMap = new HashMap<>(declaredFields.length);
        StringBuilder questionMarks = new StringBuilder("");
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(entity.getClass().getSimpleName()).append(" (");
        int index = 1;
        for (int i = 0; i < declaredFields.length; i++) {
            if (indexFieldNameMap.size() > 0) {
                query.append(",");
                questionMarks.append(",");
            }
            Field field = declaredFields[i];
            if (field.isAnnotationPresent(Id.class)) {
                continue;
            }
            indexFieldNameMap.put(index++, declaredFields[i]);
            query.append(field.getName());
            questionMarks.append("?");
        }
        query.append(") VALUES (").append(questionMarks).append(")");
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(query.toString());
            for (Map.Entry<Integer, Field> entry : indexFieldNameMap.entrySet()) {
                index = entry.getKey();
                Field field = entry.getValue();
                setPreparedStatementParameter(entity, stmt, index, field);
            }
        } finally {
            DbUtils.closeQuietly(stmt);
        }
        return stmt;
    }

    private void setPreparedStatementParameter(T entity, PreparedStatement stmt, int index, Field field) throws SQLException {
        boolean accessible = field.isAccessible();
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
