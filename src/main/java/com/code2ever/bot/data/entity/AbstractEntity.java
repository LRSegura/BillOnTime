package com.code2ever.bot.data.entity;

import com.code2ever.bot.api.annotations.InjectedDate;
import com.code2ever.bot.api.persistence.validations.HibernateEventHandlers;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;


@Getter
@ToString
@MappedSuperclass
@EntityListeners(HibernateEventHandlers.class)
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Long id;

    @Column(name = "Register_Date", nullable = false)
    @InjectedDate
    private LocalDateTime registerDate;

    @Version
    @Column(name = "OptLock")
    private int version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractEntity that)) return false;
        return version == that.version && Objects.equals(id, that.id) && Objects.equals(registerDate, that.registerDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, registerDate, version);
    }
}
