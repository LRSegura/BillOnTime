package com.code2ever.bot.data.service;

import com.code2ever.bot.data.entity.BatchPayment;
import com.code2ever.bot.data.entity.Bill;
import com.code2ever.bot.data.entity.PendingPayment;
import com.code2ever.bot.data.service.batch.BatchService;
import com.code2ever.bot.data.service.bill.BillService;
import com.code2ever.bot.data.service.pendingpayment.PendingPaymentService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


public class PendingPaymentRegister {

    private final BillService billService;
    private final PendingPaymentService pendingPaymentService;

    private final BatchService batchService;

    public PendingPaymentRegister(BillService billService, PendingPaymentService pendingPaymentService, BatchService batchService) {
        this.billService = billService;
        this.pendingPaymentService = pendingPaymentService;
        this.batchService = batchService;
    }


    //    @Scheduled(fixedDelay = 5000)
    @Scheduled(cron = "0/5 * * ? * *")
    @Transactional
    public void register() {
//        List<Bill> bills = billService.findAll();
//        BatchPayment batchPayment = new BatchPayment();
//        batchPayment.setBatchDate(LocalDate.now());
//        batchPayment.setIsBatchPaid(false);
//        batchService.save(batchPayment);
//        LocalDate now = LocalDate.now();
//        for (Bill bill : bills) {
//            PendingPayment pendingPayment = new PendingPayment();
//            pendingPayment.setBill(bill);
//            LocalDate initialDate = LocalDate.of(now.getYear(), now.getMonth().getValue(), bill.getBillingDay());
//            pendingPayment.setInitialDate(initialDate);
//            pendingPayment.setLimitDate(initialDate.plusDays(bill.getDeadLine()));
//            pendingPayment.setIsPaid(false);
//            pendingPayment.setBatchPayments(batchPayment);
//            pendingPaymentService.save(pendingPayment);
//            System.out.println(pendingPayment);
//        }
    }
}
