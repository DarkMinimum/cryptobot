package com.crypto.cryptobot.components;

import com.crypto.cryptobot.dto.Currency;
import com.crypto.cryptobot.dto.CurrencyDTO;
import org.springframework.stereotype.Component;

@Component
public class CurrencyConvertor {

    public void fromDTO(CurrencyDTO source, Currency target) {
       target.setPrice(String.valueOf(source.getPrice()));
       target.setSymbol(source.getSymbol());
    }

    public void toDTO(Currency source, CurrencyDTO target) {
        target.setPrice(Double.valueOf(source.getPrice()));
        target.setSymbol(source.getSymbol());
    }
}
