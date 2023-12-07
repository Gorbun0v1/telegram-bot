package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.service.NotificationService;

import java.util.List;
@Service
public class NotificationScheduler {
    private final NotificationService notificationService;

    public NotificationScheduler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Autowired
    private TelegramBot telegramBot;

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
