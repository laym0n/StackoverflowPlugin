package com.victor.kochnev.plugin.stackoverflow.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedDate;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class BaseEntity {
    @Id
    @GeneratedValue(generator = "platform")
    @GenericGenerator(name = "platform",
            parameters = @Parameter(name = "prefix", value = "prod"),
            strategy = "com.victor.kochnev.plugin.stackoverflow.entity.identifier.IdGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @CreatedDate
    @Column(name = "create_date")
    private ZonedDateTime createDate;
    @Column(name = "last_change_date")
    private ZonedDateTime lastChangeDate;
    @Version
    @Column(name = "version")
    private Long version;

    @PreUpdate
    void onUpdate() {
        setLastChangeDate(ZonedDateTime.now());
    }

    @PrePersist
    void onCreate() {
        ZonedDateTime now = ZonedDateTime.now();
        setCreateDate(now);
        setLastChangeDate(now);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
