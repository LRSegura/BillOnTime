package com.code2ever.bot.data.service;

import com.code2ever.bot.data.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill,Long> {
}
