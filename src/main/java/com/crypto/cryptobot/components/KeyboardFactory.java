package com.crypto.cryptobot.components;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public class KeyboardFactory {
    public static ReplyKeyboard getPizzaOrDrinkKeyboard(){
        KeyboardRow row = new KeyboardRow();
        row.add("Interupt monitoring");
        row.add("Start monitoring");
        return new ReplyKeyboardMarkup(List.of(row));
    }
}