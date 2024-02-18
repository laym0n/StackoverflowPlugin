package com.victor.kochnev.plugin.stackoverflow.entity.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import lombok.SneakyThrows;

public abstract class JsonConverter<X> implements AttributeConverter<X, String> {
    protected ObjectMapper mapper = new ObjectMapper();

    protected abstract Class<X> getMappingClass();

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(X attribute) {
        if (attribute == null) {
            return null;
        }
        return mapper.writeValueAsString(attribute);
    }

    @SneakyThrows
    @Override
    public X convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return mapper.readValue(dbData, getMappingClass());
    }
}
