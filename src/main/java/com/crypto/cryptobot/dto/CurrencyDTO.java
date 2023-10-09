package com.crypto.cryptobot.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class CurrencyDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long currencyId;
    private String symbol;
    private double price;
}
