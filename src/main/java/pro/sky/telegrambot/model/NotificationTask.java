package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "notification_task")
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "message")
    private String message;
    @Column(name = "time")
    private LocalDateTime time;

    public NotificationTask() {
    }

    public NotificationTask(Long chatId, String message, LocalDateTime time) {
        this.chatId = chatId;
        this.message = message;
        this.time = time;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }


}
