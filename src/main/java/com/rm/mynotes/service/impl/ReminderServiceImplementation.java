package com.rm.mynotes.service.impl;

import com.rm.mynotes.model.Note;
import com.rm.mynotes.model.Reminder;
import com.rm.mynotes.model.UserEntity;
import com.rm.mynotes.repository.NoteRepository;
import com.rm.mynotes.repository.ReminderRepository;
import com.rm.mynotes.repository.UserRepository;
import com.rm.mynotes.service.mold.ReminderService;
import com.rm.mynotes.utils.constants.RoutePaths;
import com.rm.mynotes.utils.constants.StatusTypes;
import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import com.rm.mynotes.utils.dto.requests.ReminderDTO;
import com.rm.mynotes.utils.functions.CommonFunctions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReminderServiceImplementation implements ReminderService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private CommonFunctions commonFunctions;

    @Override
    public ResponseEntity<Reminder> createReminder(Authentication authentication, ReminderDTO reqReminder, Long noteId) {
        UserEntity user = commonFunctions.getCurrentUser(authentication);
        Note note = user.getNotes().stream().filter(userNote -> Objects.equals(userNote.getId(), noteId))
                .findFirst().orElseThrow(() -> new BadCredentialsException("A anotação informada não existe."));

        Reminder reminder = Reminder.builder()
                .reminderDate(reqReminder.getReminderDate())
                .createdAt(CommonFunctions.getCurrentDatetime())
                .lastUpdate(CommonFunctions.getCurrentDatetime())
                .status(StatusTypes.PENDING)
                .build();

        List<Reminder> reminders = note.getReminders();
        reminders.add(reminderRepository.save(reminder));

        note.setReminders(reminders);
        noteRepository.save(note);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(RoutePaths.REMINDER).toUriString());

        return ResponseEntity.created(uri).body(reminder);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleException(Exception exception) {
        return CommonFunctions.errorHandling(exception);
    }
}
