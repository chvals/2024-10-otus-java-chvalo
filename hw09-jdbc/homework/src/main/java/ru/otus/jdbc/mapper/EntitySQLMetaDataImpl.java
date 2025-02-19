package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData{
    private EntityClassMetaData metaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData metaData) {
        this.metaData = metaData;
    }

    @Override
    public String getSelectAllSql() {
        String selectFields = ((List<Field>) metaData.getAllFields()).stream().map(f->f.getName()).collect(Collectors.joining(","));
        String sql = String.format("select %s from %s", selectFields, metaData.getName());
        return sql;
    }

    @Override
    public String getSelectByIdSql() {
        String selectFields = ((List<Field>) metaData.getAllFields()).stream().map(f->f.getName()).collect(Collectors.joining(","));
        String sql = String.format("select %s from %s where %s = ?", selectFields, metaData.getName(), metaData.getIdField().getName());
        return sql;
    }

    @Override
    public String getInsertSql() {
        String insertFields = ((List<Field>) metaData.getFieldsWithoutId()).stream().map(f->f.getName()).collect(Collectors.joining(","));
        String insertValues = ((List<Field>) metaData.getFieldsWithoutId()).stream().map(s->"?").collect(Collectors.joining(","));
        String sql = String.format("insert into %s(%s) values(%s)", metaData.getName(), insertFields, insertValues);
        return sql;
    }

    @Override
    public String getUpdateSql() {
        String updateFields = ((List<Field>) metaData.getFieldsWithoutId()).stream().map(f->f.getName()).collect(Collectors.joining(" = ?,")) + " = ?";
        String sql = String.format("update %s set %s where %s = ?", metaData.getName(), updateFields, metaData.getIdField().getName());
        return sql;
    }
}
