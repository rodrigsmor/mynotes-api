package com.rm.mynotes.utils.dto.payloads;

import com.rm.mynotes.model.Note;
import lombok.*;

import java.time.Duration;
import java.time.OffsetDateTime;

@Setter
@Getter
public class SummaryDeletedNoteDTO extends NoteSummaryDTO {
    private long remainingDays;
    private OffsetDateTime deletionDate;

    public SummaryDeletedNoteDTO(Note note) {
        super(note);
        this.deletionDate = note.getDeletionDate();
        updateRemainingDays();
    }

    public void updateRemainingDays() {
        OffsetDateTime reachedDate = this.getDeletionDate().plusDays(30);
        Duration duration = Duration.between(OffsetDateTime.now(), reachedDate);
        this.setRemainingDays(duration.toDays());
    }
}
