package com.rm.mynotes.resource;

import com.rm.mynotes.model.Reminder;
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
    public Reminder createEvent(Reminder reminder) {
        kafkaTemplate.send("remindersTopic", reminder.toString());
        return reminder;
    }
}
