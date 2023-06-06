package com.rm.mynotes.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketResource {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @MessageMapping("/createReminder")
    @SendTo("/topic/reminders")
    public Object createEvent(Object reminder) {
        kafkaTemplate.send("remindersTopic", reminder.toString());
        return reminder;
    }
}
