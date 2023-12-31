package com.code2ever.bot.data.service.pendingpayment;

import com.code2ever.bot.data.entity.BatchPayment;
import com.code2ever.bot.data.entity.PendingPayment;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class PendingPaymentService {

    private final PendingPaymentRepository repository;

    public PendingPaymentService(PendingPaymentRepository repository) {
        this.repository = repository;
    }

    public void save(PendingPayment pendingPayment){
        repository.save(pendingPayment);
//        repository.fin
    }

    public List<PendingPayment> findAll(){
        return repository.findAll();
    }
    public Collection<PendingPayment> findAllByBatchPayment(BatchPayment batchPayment){
        return repository.findPendingPaymentByBatchPayments(batchPayment);
    }

}
