package com.rm.mynotes.resource;

import com.rm.mynotes.model.Reminder;
import com.rm.mynotes.service.mold.ReminderService;
import com.rm.mynotes.utils.dto.requests.ReminderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ReminderResource {
    @Autowired
    private ReminderService reminderService;

    @GetMapping("/app/reminder/1")
    public String test() {
        return "você fez uma requisição!";
    }

    @PostMapping("/app/reminder/{noteId}")
    public ResponseEntity<Reminder> createReminder(Authentication authentication, @RequestBody ReminderDTO reminderDTO, @RequestParam(required = true, name = "noteId") Long noteId) {
        return reminderService.createReminder(authentication, reminderDTO, noteId);
    }
}
