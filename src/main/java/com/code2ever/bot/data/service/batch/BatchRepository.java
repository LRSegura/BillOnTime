package com.code2ever.bot.data.service.batch;

import com.code2ever.bot.data.entity.BatchPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchRepository extends JpaRepository<BatchPayment, Long> {
}
