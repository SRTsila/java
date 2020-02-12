import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/start":
                    sendMsgCom1(message, "Поехали!");
                case "/help":
                    sendMsgCom1(message, "Это бот, показывающий погоду на ближайшие три дня. Чтобы узнать погоду, " +
                            "введите запрос, например : Какая погода? " +
                            "После ответа бота, Вам следует ввести города на английском(!) языке," +
                            "напрмер : Yekaterinburg");
                    break;
                case "/settings":
                    sendMsgCom1(message, "Всё и так правильно работает!");
                    break;
                default:
                    try {
                        if (message.getText().contains("погод")) {
                            sendMsgCom2(message, "В каком городе?");
                        } else {
                            sendMsgCom1(message, Parser.getWeather(message.getText().toString()));
                            sendPhoto(message, message.getText().toString());
                        }
                    } catch (Exception e) {
                        sendMsgCom2(message, "Неизветсная команда или неправильно введен город.");
                    }
            }
        }
    }

    private void sendMsgCom1(Message message, String text) {
        sendMsg(message, text, "/help", "/settings", "/Какая погода?");
    }

    private void sendMsgCom2(Message message, String text) {
        sendMsg(message, text, "Yekaterinburg", "Moscow", "Paris");
    }

    private void sendMsg(Message message, String text, String command1, String command2, String command3) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(command1);
        keyboardFirstRow.add(command2);
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(command3);
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendPhoto(Message message, String city) {
        SendPhoto photo = new SendPhoto();
        photo.enableNotification();
        File p = new File(System.getProperty("user.dir") + "\\" + city + ".jpeg");
        photo.setNewPhoto(p);
        photo.setChatId(message.getChatId().toString());
        photo.setReplyToMessageId(message.getMessageId());
        try {
            sendPhoto(photo);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        String botName = System.getProperty("user.dir") + "\\src\\main\\resources\\bot_name.txt";
        try {
            return Files.readAllLines(Paths.get(botName)).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return botName;
    }

    @Override
    public String getBotToken() {
        String telegramToken = System.getProperty("user.dir") + "\\src\\main\\resources\\telegram_token.txt";
        try {
            return Files.readAllLines(Paths.get(telegramToken)).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return telegramToken;
    }

}
