package com.code2ever.bot.data.service.pendingpayment;

import com.code2ever.bot.data.entity.BatchPayment;
import com.code2ever.bot.data.entity.PendingPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PendingPaymentRepository extends JpaRepository<PendingPayment, Long> {

    Collection<PendingPayment> findPendingPaymentByBatchPayments(BatchPayment batchPayment);
}
