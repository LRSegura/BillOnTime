package com.code2ever.bot.data.service;

import com.code2ever.bot.data.entity.BatchPayment;
import com.code2ever.bot.data.entity.Bill;
import com.code2ever.bot.data.entity.PendingPayment;
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
    public void register(){
//        List<Bill> bills = billService.findAll();
//        BatchPayment batchPayment = new BatchPayment();
//        batchPayment.setBatchDate(LocalDate.now());
//        batchPayment.setIsBatchPaid(false);
//        batchService.save(batchPayment);
//        for (Bill bill:bills) {
//            PendingPayment pendingPayment = new PendingPayment();
//            pendingPayment.setBill(bill);
//            pendingPayment.setDate(LocalDate.now());
//            pendingPayment.setPaid(false);
//            pendingPayment.setBatchPayments(batchPayment);
//            pendingPaymentService.save(pendingPayment);
//            System.out.println(pendingPayment);
//        }
    }
}
