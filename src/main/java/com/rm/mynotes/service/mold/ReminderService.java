package com.rm.mynotes.service.mold;

import com.rm.mynotes.model.Reminder;
import com.rm.mynotes.utils.dto.requests.ReminderDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface ReminderService {
    ResponseEntity<Reminder> createReminder(Authentication authentication, ReminderDTO reqReminder, Long noteId);
}
