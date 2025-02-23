package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData{
    private String selectAllSQL;
    private String selectByIdSql;
    private String insertSql;
    private String updateSql;
    private EntityClassMetaData metaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData metaData) {
        this.metaData = metaData;
    }

    @Override
    public String getSelectAllSql() {
        if (this.selectAllSQL == null) {
            String selectFields = ((List<Field>) metaData.getAllFields()).stream().map(f -> f.getName()).collect(Collectors.joining(","));
            String sql = String.format("select %s from %s", selectFields, metaData.getName());
            this.selectAllSQL = sql;
        }
        return this.selectAllSQL;
    }

    @Override
    public String getSelectByIdSql() {
        if (this.selectByIdSql == null) {
            String selectFields = ((List<Field>) metaData.getAllFields()).stream().map(f -> f.getName()).collect(Collectors.joining(","));
            String sql = String.format("select %s from %s where %s = ?", selectFields, metaData.getName(), metaData.getIdField().getName());
            this.selectByIdSql = sql;
        }
        return this.selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        if (this.insertSql == null) {
            String insertFields = ((List<Field>) metaData.getFieldsWithoutId()).stream().map(f -> f.getName()).collect(Collectors.joining(","));
            String insertValues = ((List<Field>) metaData.getFieldsWithoutId()).stream().map(s -> "?").collect(Collectors.joining(","));
            String sql = String.format("insert into %s(%s) values(%s)", metaData.getName(), insertFields, insertValues);
            this.insertSql = sql;
        }
        return this.insertSql;
    }

    @Override
    public String getUpdateSql() {
        if (this.updateSql == null) {
            String updateFields = ((List<Field>) metaData.getFieldsWithoutId()).stream().map(f -> f.getName()).collect(Collectors.joining(" = ?,")) + " = ?";
            String sql = String.format("update %s set %s where %s = ?", metaData.getName(), updateFields, metaData.getIdField().getName());
            this.updateSql = sql;
        }
        return this.updateSql;
    }
}
