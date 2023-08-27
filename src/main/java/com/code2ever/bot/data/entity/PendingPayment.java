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
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "FK_Bill_PendingPayment"))
    private Bill bill;

    @Column(nullable = false)
    private LocalDate initialDate;

    @Column(nullable = false)
    private LocalDate limitDate;

    @Column(nullable = false)
    private Boolean isPaid;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "FK_BatchPayment_PendingPayment"))
    private BatchPayment batchPayments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PendingPayment that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(bill, that.bill) && Objects.equals(initialDate, that.initialDate) && Objects.equals(limitDate, that.limitDate) && Objects.equals(isPaid, that.isPaid) && Objects.equals(batchPayments, that.batchPayments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bill, initialDate, limitDate, isPaid, batchPayments);
    }
}
