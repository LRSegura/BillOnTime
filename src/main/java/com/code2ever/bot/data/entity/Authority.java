package com.code2ever.bot.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "Authority")
@AttributeOverride(name = "id", column = @Column(name = "Id_Authority"))
public class Authority extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @JoinColumn(name = "user")
    @ManyToOne
    private UserApp user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Authority authority)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(name, authority.name) && Objects.equals(user, authority.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, user);
    }
}
