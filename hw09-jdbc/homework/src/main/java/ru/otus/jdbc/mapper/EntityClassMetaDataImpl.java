package ru.otus.jdbc.mapper;

import ru.otus.core.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T>{
    private String className;
    private Constructor constructor;
    private Field fieldId;
    private List<Field> fieldList;
    private List<Field> fieldListWithoutId;
    private Class<T> clazz;

    public EntityClassMetaDataImpl(Class<T> clazz) throws NoSuchMethodException {
        this.clazz = clazz;
        initMetaDataClass();
    }

    private void initMetaDataClass() throws NoSuchMethodException {
        this.className = this.clazz.getSimpleName();
        this.constructor = this.clazz.getConstructor();
        this.fieldId = Arrays.asList(this.clazz.getDeclaredFields()).stream()
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst().orElse(null);
        this.fieldList = Arrays.asList(this.clazz.getDeclaredFields());
        this.fieldListWithoutId = Arrays.asList(this.clazz.getDeclaredFields()).stream()
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return this.className;
    }

    @Override
    public Constructor getConstructor() throws NoSuchMethodException {
        return this.constructor;
    }

    @Override
    public Field getIdField() {
        return this.fieldId;
    }

    @Override
    public List<Field> getAllFields() {
        return this.fieldList;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return this.fieldListWithoutId;
    }
}
