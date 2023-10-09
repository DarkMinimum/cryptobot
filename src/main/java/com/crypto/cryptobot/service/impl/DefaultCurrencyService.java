package com.crypto.cryptobot.service.impl;

import com.crypto.cryptobot.dto.Currency;
import com.crypto.cryptobot.dto.CurrencyDTO;
import com.crypto.cryptobot.repository.CurrencyRepository;
import com.crypto.cryptobot.service.CurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
public class DefaultCurrencyService implements CurrencyService {

    private final Curr9098encyRepository repository;
    private final


    public DefaultCurrencyService(CurrencyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Currency save(Currency currency) {
        return repository.save(currency);
    }

    @Override
    public void saveAll(List<Currency> currency) {
        repository.saveAll(currency);
    }

    @Override
    public Iterable<Currency> getAll() {
        return repository.findAll();
    }

    @Override
    public void doGet() {
        OkHttpClient client = new OkHttpClient();

        // Create a request object
        Request request = new Request.Builder().url("https://api.mexc.com/api/v3/ticker/price").build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                final String body = (response.body().string());
                ObjectMapper mapper = new ObjectMapper();
                List<Currency> currencies = mapper.readValue(body, mapper.getTypeFactory().constructCollectionType(List.class, Currency.class));
                List<CurrencyDTO> currencyDTOS =
                
                this.saveAll(currencies);
                this.getAll().forEach(System.out::println);
            } else {
                System.out.println("Request failed with code: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private List<Currency> validatePrice(List<Currency> currencies) {
        for (:
             ) {

        }
    }
}
