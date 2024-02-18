package com.victor.kochnev.plugin.stackoverflow.entity.converter;

import lombok.SneakyThrows;

import java.util.List;

public abstract class ListJsonConverter<X> extends JsonConverter<List<X>> {

    protected abstract Class<X> getInnerClass();

    @Override
    protected Class<List<X>> getMappingClass() {
        return (Class<List<X>>) (Object) List.class;
    }

    @SneakyThrows
    @Override
    public List<X> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        Class<X> innerClass = getInnerClass();
        return mapper.readValue(dbData, mapper.getTypeFactory().constructCollectionType(List.class, innerClass));
    }
}
