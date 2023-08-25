package com.code2ever.bot.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
public class Bill extends AbstractEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private Integer billingDay;

    @Column()
    private Integer deadLine;

    @Column()
    private Boolean isCreditCard;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bill bill)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(name, bill.name) && Objects.equals(total, bill.total) && Objects.equals(billingDay, bill.billingDay) && Objects.equals(deadLine, bill.deadLine) && Objects.equals(isCreditCard, bill.isCreditCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, total, billingDay, deadLine, isCreditCard);
    }
}
