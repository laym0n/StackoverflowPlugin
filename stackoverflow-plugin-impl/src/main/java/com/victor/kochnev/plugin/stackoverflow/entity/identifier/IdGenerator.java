package com.victor.kochnev.plugin.stackoverflow.entity.identifier;

import com.victor.kochnev.plugin.stackoverflow.entity.BaseEntity;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.Objects;
import java.util.UUID;

public class IdGenerator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        BaseEntity entity = (BaseEntity) o;
        if (Objects.isNull(entity.getId())) {
            return UUID.randomUUID();
        } else {
            return entity.getId();
        }
    }
}
