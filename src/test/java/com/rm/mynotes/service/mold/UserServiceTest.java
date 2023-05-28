package com.rm.mynotes.service.mold;

import com.rm.mynotes.model.Note;
import com.rm.mynotes.model.UserEntity;
import com.rm.mynotes.repository.UserRepository;
import com.rm.mynotes.service.impl.UserServiceImplementation;
import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import com.rm.mynotes.utils.dto.payloads.UserOverviewDTO;
import com.rm.mynotes.utils.functions.CommonFunctions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserServiceImplementation userService;

    @Mock
    CommonFunctions commonFunctions;

    @Mock
    private Authentication authentication;

    UserEntity user;

    @BeforeEach
    public void setUp() {
        List<Note> notes = new ArrayList<>();
        notes.add(Note.builder().isExcluded(false).createdAt(OffsetDateTime.parse("2022-05-28T10:00:00+00:00")).build());
        notes.add(Note.builder().isExcluded(true).createdAt(OffsetDateTime.parse("2021-04-13T10:00:00+00:00")).build());
        notes.add(Note.builder().isExcluded(false).createdAt(OffsetDateTime.now().minusMonths(2)).build());
        notes.add(Note.builder().isExcluded(true).createdAt(OffsetDateTime.now()).build());
        notes.add(Note.builder().isExcluded(false).createdAt(OffsetDateTime.now()).build());
        notes.add(Note.builder().isExcluded(false).createdAt(OffsetDateTime.now().withDayOfMonth(1)).build());

        user = UserEntity.builder()
                .notes(notes)
                .build();
    }

    @Test
    void shouldReturnAnOverviewOfUserNotes() {
        when(commonFunctions.getCurrentUser(authentication)).thenReturn(user);

        ResponseEntity<ResponseDTO> response = userService.getUserOverview(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserOverviewDTO userOverviewDTO = (UserOverviewDTO) response.getBody().getData();
        assertEquals(2, userOverviewDTO.getLastMonth());
        assertEquals(4, userOverviewDTO.getTotalNotes());
        assertEquals(2, userOverviewDTO.getNotesDeleted());
    }
}
