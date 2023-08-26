package com.code2ever.bot.data.service.batch;

import com.code2ever.bot.data.entity.BatchPayment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchService {

    private final BatchRepository batchRepository;

    public BatchService(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    public void save(BatchPayment batchPayment){
        batchRepository.save(batchPayment);
    }

    public List<BatchPayment> findAll(){
        return batchRepository.findAll();
    }
}
