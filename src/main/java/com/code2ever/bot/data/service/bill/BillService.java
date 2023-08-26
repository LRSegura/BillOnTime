package com.code2ever.bot.data.service.bill;

import com.code2ever.bot.data.entity.Bill;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillService {

    private final BillRepository repository;


    public BillService(BillRepository repository) {
        this.repository = repository;
    }

    public void save(Bill bill){
        repository.save(bill);
    }
    public void delete(Bill bill){
        repository.delete(bill);
    }
    public List<Bill> findAll(){
        return repository.findAll();
    }
}
