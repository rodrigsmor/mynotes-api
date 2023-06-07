package com.rm.mynotes.utils.dto.requests;

import com.rm.mynotes.utils.constants.StatusTypes;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
public class ReminderDTO {
    @NotNull(message = "O título não pode está vázio")
    private String title;

    @NotNull(message = "Deve haver ao menos uma data")
    private OffsetDateTime reminderDate;
}
