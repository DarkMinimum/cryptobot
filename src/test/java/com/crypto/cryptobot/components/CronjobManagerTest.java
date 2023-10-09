package com.crypto.cryptobot.components;

import com.crypto.cryptobot.dto.CurrencyDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CronjobManagerTest {

    @Test
    public void testDiff() {
        CronjobManager manager = new CronjobManager(null, null);

        CurrencyDTO newDto = new CurrencyDTO();
        newDto.setPrice(55);

        CurrencyDTO oldDTO = new CurrencyDTO();
        oldDTO.setPrice(15);

        Assertions.assertTrue(manager.isChanged(oldDTO, newDto));
    }

    @Test
    public void testDiff1() {
        CronjobManager manager = new CronjobManager(null, null);

        CurrencyDTO newDto = new CurrencyDTO();
        newDto.setPrice(110);

        CurrencyDTO oldDTO = new CurrencyDTO();
        oldDTO.setPrice(100);

        Assertions.assertTrue(manager.isChanged(oldDTO, newDto));
    }

    @Test
    public void testDiff2() {
        CronjobManager manager = new CronjobManager(null, null);

        CurrencyDTO newDto = new CurrencyDTO();
        newDto.setPrice(109);

        CurrencyDTO oldDTO = new CurrencyDTO();
        oldDTO.setPrice(100);

        Assertions.assertFalse(manager.isChanged(oldDTO, newDto));
    }

}