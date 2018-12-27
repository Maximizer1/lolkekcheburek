package com.company;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;


public class Bot extends TelegramLongPollingBot {

    private static final String BOT_NAME = "max_one_bot";
    private static final String BOT_TOKEN = "702845899:AAGts1lW572OOx3THZui66rO8fJOdolWB70";
    UserManager manager = new UserManager();
    int idWord = 1;
    int counter;
    public long chat_id;
    String whichChoice = "";
    String addWord = "";
    String status = "";
    String[] subStr;
    boolean ifUserWriteWord = false;
    boolean isImpDuplicate = true;
    boolean isNotImpDuplicate = true;
    int isImpDuplicat = 0;
    int isNotImpDuplicat = 0;

    @Override
    public void onUpdateReceived(Update update) {
        ArrayList<String> arrayList = new ArrayList<>();
        String words;
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            chat_id = update.getMessage().getChatId();
            switch (message.getText()) {
                case "/start":
                    sendMsg(chat_id, "Це бот який допоможе мені запам'ятати важливі англійські слова.", MarkupInlineOnStart());
                    counter = 1;
                    break;
                default:
                    switch (whichChoice) {
                        case "1":
                            manager.showAllList();
                            break;
                        case "2":
                            addWord = message.getText();
                            sendMsg(chat_id, "Обери статус цього слова", MarkupInlineOnChoice2());
                            break;
                        case "3":

                            break;
                        case "4":

                            break;
                    }
                    break;
            }
        } else if (update.hasCallbackQuery()) {
            String callData = update.getCallbackQuery().getData();
            System.out.println(callData);
            int msg_id = update.getCallbackQuery().getMessage().getMessageId();
            switch (callData) {
                case "/startprogram":
                    sendMsg(chat_id, "Отже розпочнемо");
                    sendMsg(chat_id, "Обери що хочеш зробити", MarkupInlineOnChoice1());
                    editMsg(chat_id, msg_id);
                    break;
                case "/showlist":
                    editMsg(chat_id, msg_id);
                    whichChoice = "1";
                    break;
                case "/addword":
                    sendMsg(chat_id, "Введи слово яке хочеш додати");
                    editMsg(chat_id, msg_id);
                    whichChoice = "2";
                    break;
                case "/showimportant":
                    arrayList = manager.showAllList("Important");
                    words = "";
                    for (String s: arrayList){
                        words = words + s + "\n";
                    }
                    editMessage(update, words);
                    whichChoice = "3";
                    break;
                case "/shownotimportant":
                    arrayList = manager.showAllList("no");
                    words = "";
                    for (String s: arrayList){
                        words = words + s + "\n";
                    }
                    editMessage(update, words);
                    whichChoice = "4";
                    break;
                case "/important":
                    while (isImpDuplicat != 1) {
                        isImpDuplicate = manager.isIdDuplicateInImportant(idWord);
                        if (isImpDuplicate == true) {
                            ++idWord;
                        } else {
                            isImpDuplicat = 1;
                        }
                    }
                    status = "important";
                    manager.addImportantAnswer(idWord, addWord, status);
                    sendMsg(chat_id, "Слово успішно додано");
                    editMsg(chat_id, msg_id);
                    break;
                case "/notimportant":
                    while (isNotImpDuplicat != 1) {
                        isNotImpDuplicate = manager.isIdDuplicateInNotImportant(idWord);
                        if (isNotImpDuplicate == true) {
                            ++idWord;
                        } else {
                            isNotImpDuplicat = 1;
                        }
                    }
                    status = "NOTimportant";
                    manager.addNotImportantAnswer(idWord, addWord, status);
                    sendMsg(chat_id, "Слово успішно додано");
                    editMsg(chat_id, msg_id);
                    break;
            }
        }
    }

    public void editMsg(long chatID, int msgID) {
        EditMessageReplyMarkup editMessage = new EditMessageReplyMarkup()
                .setMessageId(msgID)
                .setChatId(chatID)
                .setReplyMarkup(null);

        try {
            execute(editMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(long chat_id, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chat_id);
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(long chat_id, String text, InlineKeyboardMarkup markupInline) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chat_id);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(markupInline);
        try {
            execute(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public InlineKeyboardMarkup MarkupInlineOnStart() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<List<InlineKeyboardButton>>();
        //
        List<InlineKeyboardButton> rowInline = new ArrayList<InlineKeyboardButton>();

        rowInline.add(new InlineKeyboardButton().setText("Start").setCallbackData("/startprogram"));

        //Set the keyboard to the markup
        rowsInline.add(rowInline);
        //Add it to the message
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    public InlineKeyboardMarkup MarkupInlineOnChoice1() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<List<InlineKeyboardButton>>();
        //
        List<InlineKeyboardButton> rowInline1 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> rowInline3 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> rowInline4 = new ArrayList<InlineKeyboardButton>();


        rowInline2.add(new InlineKeyboardButton().setText("Додати слово").setCallbackData("/addword"));
        rowInline3.add(new InlineKeyboardButton().setText("Показати важливі").setCallbackData("/showimportant"));
        rowInline4.add(new InlineKeyboardButton().setText("Показати неважливі").setCallbackData("/shownotimportant"));

        //Set the keyboard to the markup
        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        rowsInline.add(rowInline3);
        rowsInline.add(rowInline4);
        //Add it to the message
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    public InlineKeyboardMarkup MarkupInlineOnChoice2() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<List<InlineKeyboardButton>>();
        //
        List<InlineKeyboardButton> rowInline1 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<InlineKeyboardButton>();

        rowInline1.add(new InlineKeyboardButton().setText("Важливе").setCallbackData("/important"));
        rowInline2.add(new InlineKeyboardButton().setText("Неважливе").setCallbackData("/notimportant"));

        //Set the keyboard to the markup
        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        //Add it to the message
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    public void editMessage(Update update, String answer){
        EditMessageText new_message = new EditMessageText()
                .setChatId(update.getCallbackQuery().getMessage().getChatId())
                .setMessageId(toIntExact(update.getCallbackQuery().getMessage().getMessageId()))
                .setText(answer);
        try {
            execute(new_message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
