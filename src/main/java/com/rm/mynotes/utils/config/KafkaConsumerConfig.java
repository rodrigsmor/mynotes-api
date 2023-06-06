package com.rm.mynotes.utils.config;

import com.rm.mynotes.model.Notification;
import com.rm.mynotes.model.Reminder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "remindersTopic", groupId = "group1")
    public void listen(String message) {
        Reminder reminder = parseReminder(message);
        Notification notification = new Notification();
        messagingTemplate.convertAndSend("/app/topic/notifications", notification);
    }

    private Reminder parseReminder(String message) {
        return new Reminder();
    }
}
