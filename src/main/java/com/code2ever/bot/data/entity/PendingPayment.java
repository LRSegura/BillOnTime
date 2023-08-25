package com.code2ever.bot.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Entity
@ToString
public class PendingPayment extends AbstractEntity {

    @ManyToOne
    @JoinColumn
    private Bill bill;

    @Column
    private LocalDate date;

    @Column
    private Boolean paid;

    @ManyToOne(optional = false)
    @JoinColumn
    private BatchPayment batchPayments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PendingPayment that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(bill, that.bill) && Objects.equals(date, that.date) && Objects.equals(paid, that.paid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bill, date, paid);
    }




}
