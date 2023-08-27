package com.code2ever.bot.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@ToString
public class BatchPayment extends AbstractEntity {

    @Column
    private LocalDate batchDate;

    @Column
    private Boolean isBatchPaid;

//    @OneToMany(mappedBy = "batchPayments")
//    private List<PendingPayment> pendingPayment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BatchPayment that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(isBatchPaid, that.isBatchPaid) && Objects.equals(batchDate, that.batchDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isBatchPaid, batchDate);
    }
}
