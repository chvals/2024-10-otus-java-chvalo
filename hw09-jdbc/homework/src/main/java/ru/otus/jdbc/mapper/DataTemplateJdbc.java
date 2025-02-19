package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.otus.core.Id;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.crm.model.Client;

/** Сохратяет объект в базу, читает объект из базы */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;

    private final EntityClassMetaData entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    T obj = (T) entityClassMetaData.getConstructor().newInstance();
                    for (Field field : (List<Field>)entityClassMetaData.getAllFields()) {
                        field.setAccessible(true);
                        field.set(obj, rs.getObject(field.getName()));
                    }
                    return obj;
                }
                return null;
            } catch (SQLException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor
                .executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
                    var objList = new ArrayList<T>();
                    try {
                        while (rs.next()) {
                            T obj = (T) entityClassMetaData.getConstructor().newInstance();
                            for (Field field : (List<Field>)entityClassMetaData.getAllFields()) {
                                field.setAccessible(true);
                                field.set(obj, rs.getObject(field.getName()));
                            }
                            objList.add(obj);
                        }
                        return objList;
                    } catch (SQLException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                             InvocationTargetException e) {
                        throw new DataTemplateException(e);
                    }
                })
                .orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            List<Object> param = new ArrayList<>();
            for (Field field : (List<Field>)entityClassMetaData.getFieldsWithoutId()) {
                field.setAccessible(true);
                param.add(field.get(client));
            }
            return dbExecutor.executeStatement(
                    connection, entitySQLMetaData.getInsertSql(), param);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            List<Object> param = new ArrayList<>();
            for (Field field : (List<Field>)entityClassMetaData.getFieldsWithoutId()) {
                field.setAccessible(true);
                param.add(field.get(client));
            }
            Field fieldId = entityClassMetaData.getIdField();
            fieldId.setAccessible(true);
            param.add(fieldId.get(client));
            dbExecutor.executeStatement(
                    connection, entitySQLMetaData.getUpdateSql(), param);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
