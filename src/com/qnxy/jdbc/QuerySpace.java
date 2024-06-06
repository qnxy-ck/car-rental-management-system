package com.qnxy.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.qnxy.jdbc.Utils.closeAction;

/**
 * @author Qnxy
 */
@Slf4j
@RequiredArgsConstructor
public final class QuerySpace<T> {

    private static final Map<Class<?>, Map<String, Field>> FIELD_CACHE = new ConcurrentHashMap<>();
    private final List<Object> paramList = new ArrayList<>();
    private final Class<T> entityClass;
    private String sql;

    private static <T> Map<String, Field> getFields(Class<T> entityClass) {
        final Map<String, Field> fieldMap = FIELD_CACHE.computeIfAbsent(entityClass, it -> new ConcurrentHashMap<>());
        if (!fieldMap.isEmpty()) {
            return fieldMap;
        }

        final Field[] declaredFields = entityClass.getDeclaredFields();
        for (final Field field : declaredFields) {
            fieldMap.put(field.getName(), field);
        }

        return fieldMap;
    }

    private static String columnNameToFieldName(String columnName) {
        String[] words = columnName.split("_");

        final StringBuilder camelCase = new StringBuilder(words[0].toLowerCase());
        for (int i = 1; i < words.length; i++) {
            final String word = words[i];
            camelCase.append(word.substring(0, 1).toUpperCase());
            camelCase.append(word.substring(1).toLowerCase());
        }

        return camelCase.toString();
    }

    public QuerySpace<T> sql(String sql) {
        this.sql = sql;
        return this;
    }

    public QuerySpace<T> params(Object... params) {
        this.paramList.addAll(Arrays.asList(params));
        return this;
    }

    public QuerySpace<T> param(Object param) {
        this.paramList.add(param);
        return this;
    }

    public Optional<T> optional() {
        return this.list().stream().findFirst();
    }

    public int count() {
        final DataSource dataSource = DataSourceConfiguration.dataSource();
        try {
            final Connection connection = dataSource.getConnection();
            final PreparedStatement ps = connection.prepareStatement(sql);
            for (int i = 0; i < this.paramList.size(); i++) {
                ps.setObject(i + 1, this.paramList.get(i));
            }
            final ResultSet resultSet = ps.executeQuery();
            int count = -1;
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

            closeAction(connection, ps, resultSet);
            return count;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<T> list() {
        final DataSource dataSource = DataSourceConfiguration.dataSource();
        try {
            final Connection connection = dataSource.getConnection();
            final PreparedStatement ps = connection.prepareStatement(sql);
            for (int i = 0; i < this.paramList.size(); i++) {
                ps.setObject(i + 1, this.paramList.get(i));
            }
            final ResultSet resultSet = ps.executeQuery();
            final List<T> list = resultTransform(resultSet);
            closeAction(connection, ps, resultSet);
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<T> resultTransform(ResultSet resultSet) {
        final List<T> list = new ArrayList<>();

        try {
            while (resultSet.next()) {
                final ResultSetMetaData metaData = resultSet.getMetaData();
                final Map<String, Field> fieldMap = getFields(this.entityClass);

                final T instance = createInstance();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    final String columnName = metaData.getColumnName(i + 1);
                    final String fieldName = columnNameToFieldName(columnName);

                    final Object v = resultSet.getObject(columnName);
                    final Field field = fieldMap.get(fieldName);

                    this.setResultVal(instance, field, v);
                }

                list.add(instance);
            }

        } catch (SQLException e) {
            log.error("查询出错", e);
        }

        return list;
    }

    private void setResultVal(Object obj, Field field, Object v) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        try {
            field.set(obj, v);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public T createInstance() {
        try {
            return entityClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
