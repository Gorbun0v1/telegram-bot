package pro.sky.telegrambot.listener;
import org.springframework.scheduling.annotation.Scheduled;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.service.NotificationService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final NotificationService notificationService;

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    public TelegramBotUpdatesListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
//        updates.forEach(update -> {
//            logger.info("Processing update: {}", update);
//            update.message().text().equals("/start");
//        });

        for (Update update:updates) {
            if (update.message() != null) {
                if (update.message().text().equals("/start")) {
                    sendMassage(update.message().chat().id(), "Привет, " + update.message().chat().firstName());
                } else if (update.message().text().matches("([0-9.:\\s]{16})(\\s)([\\W+]+)")) {
                    parseMessage(update.message().chat().id(),update.message().text());
                }
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void parseMessage(Long id, String text) {
        Pattern pattern = Pattern.compile("([0-9.:\\s]{16})(\\s)([\\W+]+)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            String date = matcher.group(1);
            String messageText = matcher.group(3);
            notificationService.createNotification(id, messageText, date);
        }

    }

    public void sendMassage(Long chatId, String massageText) {
        SendMessage sendMessage = new SendMessage(chatId, massageText);
        telegramBot.execute(sendMessage);
    }
    @Scheduled(cron = "0 * * * * *")
    public void run() {
        List<NotificationTask> notificationTasks = notificationService.getCurrentNotifications();
        for (NotificationTask notificationTask:notificationTasks) {
            sendMassage(notificationTask.getChatId(), notificationTask.getMessage());
        }
    }

}
