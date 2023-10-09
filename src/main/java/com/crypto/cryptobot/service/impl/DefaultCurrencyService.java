package com.crypto.cryptobot.service.impl;

import com.crypto.cryptobot.components.CurrencyConvertor;
import com.crypto.cryptobot.components.CronjobManager;
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
import java.util.ArrayList;
import java.util.List;


@Service
public class DefaultCurrencyService implements CurrencyService {

    private final CurrencyRepository repository;
    private final CurrencyConvertor convertor;

    private final CronjobManager cronjobManager;


    public DefaultCurrencyService(CurrencyRepository repository, CurrencyConvertor convertor, CronjobManager cronjobManager) {
        this.repository = repository;
        this.convertor = convertor;
        this.cronjobManager = cronjobManager;
    }

    @Override
    public void save(Currency currency) {
        CurrencyDTO dto = new CurrencyDTO();
        convertor.toDTO(currency, dto);
        repository.save(dto);
    }

    @Override
    public void saveAll(List<Currency> currency) {
        List<CurrencyDTO> dtos = new ArrayList<>();
        currency.forEach(c -> {
            var target = new CurrencyDTO();
            convertor.toDTO(c, target);
            dtos.add(target);
        });
        repository.saveAll(dtos);
    }

    @Override
    public Iterable<Currency> getAll() {
        Iterable<CurrencyDTO> dtos = repository.findAll();
        List<Currency> currencies = new ArrayList<>();
        dtos.forEach(d -> {
            var target = new Currency();
            convertor.fromDTO(d, target);
            currencies.add(target);
        });

        return currencies;
    }

    @Override
    public void startMonitoring() {
        OkHttpClient client = new OkHttpClient();

        // Create a request object
        Request request = new Request.Builder().url("https://api.mexc.com/api/v3/ticker/price").build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                final String body = (response.body().string());
                ObjectMapper mapper = new ObjectMapper();
                List<Currency> currencies = mapper.readValue(body, mapper.getTypeFactory().constructCollectionType(List.class, Currency.class));
                this.saveAll(currencies);
            } else {
                System.out.println("Request failed with code: " + response.code());
            }

            cronjobManager.startJob();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopMonitoring() {
        cronjobManager.stopJob();
    }
}
