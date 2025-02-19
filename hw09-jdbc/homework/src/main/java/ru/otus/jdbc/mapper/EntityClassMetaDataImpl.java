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
    private Class<T> clazz;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return this.clazz.getSimpleName();
    }

    @Override
    public Constructor getConstructor() throws NoSuchMethodException {
        return this.clazz.getConstructor();
    }

    @Override
    public Field getIdField() {
        return Arrays.asList(this.clazz.getDeclaredFields()).stream()
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst().orElse(null);
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.asList(this.clazz.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Arrays.asList(this.clazz.getDeclaredFields()).stream()
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
    }
}
