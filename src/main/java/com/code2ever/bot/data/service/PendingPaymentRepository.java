package com.code2ever.bot.data.service;

import com.code2ever.bot.data.entity.BatchPayment;
import com.code2ever.bot.data.entity.PendingPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PendingPaymentRepository extends JpaRepository<PendingPayment, Long> {

    List<PendingPayment> findPendingPaymentByBatchPayments(BatchPayment batchPayment);
}
