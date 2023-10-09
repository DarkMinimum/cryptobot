package com.crypto.cryptobot.components;

import com.crypto.cryptobot.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ResponseHandler {

    private final int MAX_USERS = 5;
    private final List<Long> activeUsers;

    private final SilentSender sender;
    private final Map<Long, UserState> chatStates;

    private final CurrencyService currencyService;

    public ResponseHandler(SilentSender sender, DBContext db, CurrencyService currencyService) {
        this.sender = sender;
        chatStates = db.getMap(Constants.CHAT_STATES);
        this.currencyService = currencyService;
        this.activeUsers = new ArrayList<>();
    }

    public void replyToStart(long chatId) {
        log.info("User with ID: {} just logged in", chatId);
        if (!activeUsers.contains(chatId)) {
            activeUsers.add(chatId);
        }

        if (activeUsers.size() < MAX_USERS) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(Constants.START_TEXT);
            sender.execute(message);
            chatStates.put(chatId, UserState.AWAITING_NAME);
        } else {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("The bot is working with other users, please try later");
            sender.execute(message);
        }

    }

    public void replyToButtons(long chatId, Message message) {
        if (message.getText().equalsIgnoreCase("/stop")) {
            stopChat(chatId);
            log.info("User with ID: {} just logged off", chatId);
        }

        switch (chatStates.get(chatId)) {
            case AWAITING_NAME -> replyToName(chatId, message);
            case IN_MONITORING -> replayToMonitoring(chatId, message);
            default -> unexpectedMessage(chatId);
        }
    }

    private void unexpectedMessage(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("This message is unexpected");
        sender.execute(sendMessage);
    }

    private void stopChat(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("The session ended");
        chatStates.remove(chatId);
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        sender.execute(sendMessage);
    }

    private void promptWithKeyboardForState(long chatId, String text, ReplyKeyboard YesOrNo, UserState awaitingReorder) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(YesOrNo);
        sender.execute(sendMessage);
        chatStates.put(chatId, awaitingReorder);
    }

    private void replayToMonitoring(long chatId, Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if ("start monitoring".equalsIgnoreCase(message.getText())) {
            currencyService.startMonitoring();
        } else if ("interrupt monitoring".equalsIgnoreCase(message.getText())) {
            currencyService.stopMonitoring();
        } else {
            sendMessage.setText("We don't sell " + message.getText() + ". Please select from the options below.");
            sendMessage.setReplyMarkup(KeyboardFactory.getPizzaOrDrinkKeyboard());
            sender.execute(sendMessage);
        }
    }

    private void replyToName(long chatId, Message message) {
        promptWithKeyboardForState(chatId, "Hello " + message.getText() + ". What would you like to have?", KeyboardFactory.getPizzaOrDrinkKeyboard(), UserState.IN_MONITORING);
    }

    public boolean userIsActive(Long chatId) {
        return chatStates.containsKey(chatId);
    }
}