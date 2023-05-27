package com.rm.mynotes.utils.dto.payloads;

import com.rm.mynotes.model.Annotation;
import lombok.*;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Date;

@Setter
@Getter
public class SummaryDeletedNoteDTO extends AnnotationSummaryDTO {
    private long remainingDays;
    private OffsetDateTime deletionDate;

    public SummaryDeletedNoteDTO(Annotation annotation) {
        super(annotation);
        this.deletionDate = annotation.getDeletionDate();
        updateRemainingDays();
    }

    public void updateRemainingDays() {
        OffsetDateTime reachedDate = this.getDeletionDate().plusDays(30);
        Duration duration = Duration.between(OffsetDateTime.now(), reachedDate);
        this.setRemainingDays(duration.toDays());
    }
}
