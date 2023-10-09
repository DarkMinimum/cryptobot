package com.crypto.cryptobot.repository;

import com.crypto.cryptobot.dto.CurrencyDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.meta.When;

@Repository
public interface CurrencyRepository extends CrudRepository<CurrencyDTO, Long> {
}