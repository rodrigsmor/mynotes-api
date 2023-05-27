package com.rm.mynotes.utils.dto.payloads;

import com.rm.mynotes.model.Note;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.OffsetDateTime;

@Getter
@Setter
public class DeletedNoteDetailedDTO extends NoteDetailedDTO {
    private Long remainingDays;
    private OffsetDateTime deletionDate;

    public DeletedNoteDetailedDTO(Note note) {
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
