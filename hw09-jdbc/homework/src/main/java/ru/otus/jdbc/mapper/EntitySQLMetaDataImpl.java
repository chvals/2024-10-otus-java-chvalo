package ru.otus.jdbc.mapper;

import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData<T>{
    private String selectAllSQL;
    private String selectByIdSql;
    private String insertSql;
    private String updateSql;
    private final EntityClassMetaData<T> metaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> metaData) {
        this.metaData = metaData;
    }

    @Override
    public String getSelectAllSql() {
        if (this.selectAllSQL == null) {
            String selectFields = metaData.getAllFields().stream().map(f -> f.getName()).collect(Collectors.joining(","));
            String sql = String.format("select %s from %s", selectFields, metaData.getName());
            this.selectAllSQL = sql;
        }
        return this.selectAllSQL;
    }

    @Override
    public String getSelectByIdSql() {
        if (this.selectByIdSql == null) {
            String selectFields = metaData.getAllFields().stream().map(f -> f.getName()).collect(Collectors.joining(","));
            String sql = String.format("select %s from %s where %s = ?", selectFields, metaData.getName(), metaData.getIdField().getName());
            this.selectByIdSql = sql;
        }
        return this.selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        if (this.insertSql == null) {
            String insertFields = metaData.getFieldsWithoutId().stream().map(f -> f.getName()).collect(Collectors.joining(","));
            String insertValues = metaData.getFieldsWithoutId().stream().map(s -> "?").collect(Collectors.joining(","));
            String sql = String.format("insert into %s(%s) values(%s)", metaData.getName(), insertFields, insertValues);
            this.insertSql = sql;
        }
        return this.insertSql;
    }

    @Override
    public String getUpdateSql() {
        if (this.updateSql == null) {
            String updateFields = metaData.getFieldsWithoutId().stream().map(f -> f.getName()).collect(Collectors.joining(" = ?,")) + " = ?";
            String sql = String.format("update %s set %s where %s = ?", metaData.getName(), updateFields, metaData.getIdField().getName());
            this.updateSql = sql;
        }
        return this.updateSql;
    }
}
