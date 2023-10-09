package com.crypto.cryptobot.service;

import com.crypto.cryptobot.dto.Currency;

import java.util.List;

public interface CurrencyService {

    /**
     * Save one currency
     *
     * @param currency
     * @return saved entry
     */
    void save(Currency currency);

    /**
     * Save batch of currencies
     *
     * @param currency
     */
    void saveAll(List<Currency> currency);

    /**
     * Fetch batch of currencies
     *
     * @return saved batch of entries
     */
    Iterable<Currency> getAll();

    void doGet();

}
