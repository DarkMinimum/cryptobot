package com.crypto.cryptobot.components;

import com.crypto.cryptobot.dto.Currency;
import com.crypto.cryptobot.dto.CurrencyDTO;
import com.crypto.cryptobot.repository.CurrencyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class CronjobManager {


    private final double PERCENT = 10;

    private boolean taskStarted = false;
    private final CurrencyConvertor convertor;

    private final CurrencyRepository repository;

    public CronjobManager(CurrencyRepository repository, CurrencyConvertor convertor) {
        this.repository = repository;
        this.convertor = convertor;
    }

    @Scheduled(cron = "${request.frequancy.cron}")
    public void scheduleTask() {

        CurrencyDTO emptyDTO = new CurrencyDTO();
        emptyDTO.setPrice(0);
        List<CurrencyDTO> resultList = new ArrayList<>();

        if (taskStarted) {
            List<CurrencyDTO> dtos = doGet();

            List<CurrencyDTO> fromDB = new ArrayList<>();
            repository.findAll().forEach(fromDB::add);

            for (int i = 0; i < fromDB.size(); i++) {
                CurrencyDTO oldDTO = fromDB.get(i);
                String symbol = fromDB.get(i).getSymbol();
                CurrencyDTO newDTO = dtos.stream().filter(dto -> dto.getSymbol().equals(symbol)).findFirst().orElse(emptyDTO);

                if (isChanged(oldDTO, newDTO)) {
                    resultList.add(newDTO);
                    log.info("{} -- {}", oldDTO.getPrice(), newDTO.getPrice());
                    log.info("{}", newDTO);
                }

            }

            repository.deleteAll();
            repository.saveAll(dtos);


        }
    }

    public boolean isChanged(CurrencyDTO oldDto, CurrencyDTO newDto) {
        double oldPrice = oldDto.getPrice();
        double newPrice = newDto.getPrice();
        double percentageChange = ((newPrice - oldPrice) / oldPrice) * 100;
        return Math.abs(percentageChange) >= PERCENT;
    }

    private List<CurrencyDTO> doGet() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://api.mexc.com/api/v3/ticker/price").build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                final String body = (response.body().string());
                ObjectMapper mapper = new ObjectMapper();
                List<Currency> currencies = mapper.readValue(body, mapper.getTypeFactory().constructCollectionType(List.class, Currency.class));

                List<CurrencyDTO> dtos = new ArrayList<>();

                currencies.forEach(d -> {
                    CurrencyDTO target = new CurrencyDTO();
                    convertor.toDTO(d, target);
                    dtos.add(target);
                });

                return dtos;

            } else {
                return Collections.emptyList();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public void startJob() {
        taskStarted = true;
    }

    public void stopJob() {
        taskStarted = false;
    }
}
