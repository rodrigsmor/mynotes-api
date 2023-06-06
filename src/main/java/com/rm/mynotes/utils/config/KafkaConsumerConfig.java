package com.rm.mynotes.utils.config;

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
        Object reminder = parseReminder(message);
        messagingTemplate.convertAndSend("/app/topic/notifications", notification);
    }

    private Object parseReminder(String message) {
        return new Object();
    }
}
