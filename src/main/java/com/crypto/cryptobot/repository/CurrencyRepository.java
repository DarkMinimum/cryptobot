package com.crypto.cryptobot.repository;

import com.crypto.cryptobot.dto.CurrencyDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends CrudRepository<CurrencyDTO, Long> {
}