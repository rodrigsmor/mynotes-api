package com.rm.mynotes.service.impl;

import com.rm.mynotes.model.Note;
import com.rm.mynotes.model.UserEntity;
import com.rm.mynotes.service.mold.UserService;
import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import com.rm.mynotes.utils.dto.payloads.UserOverviewDTO;
import com.rm.mynotes.utils.functions.CommonFunctions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.OffsetDateTime;

@Slf4j
@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    private CommonFunctions commonFunctions;

    @Override
    public ResponseEntity<ResponseDTO> getUserOverview(Authentication authentication) {
        try {
            UserEntity user = commonFunctions.getCurrentUser(authentication);
            OffsetDateTime date = OffsetDateTime.now().withDayOfMonth(1).withSecond(0).withHour(0).minusMinutes(0).withNano(0);

            int totalNotes = user.getNotes().stream().filter(note -> !note.getIsExcluded()).toList().size();
            int notesDeleted = user.getNotes().stream().filter(Note::getIsExcluded).toList().size();
            int lastMonth = user.getNotes()
                    .stream()
                    .filter(note -> (!note.getIsExcluded() && date.isBefore(note.getCreatedAt())))
                    .toList().size();

            ResponseDTO response = ResponseDTO.builder().data(new UserOverviewDTO(lastMonth, totalNotes, notesDeleted)).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception exception) {
            return CommonFunctions.errorHandling(exception);
        }
    }
}
