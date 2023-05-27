package com.rm.mynotes.utils.dto.payloads;

import com.rm.mynotes.model.Note;
import com.rm.mynotes.utils.constants.CategoryTypes;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class NoteSummaryDTO {
    private Long id;
    private String title;
    private String description;
    private CategoryTypes category;
    private String icon;
    private String cover;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastUpdate;

    public NoteSummaryDTO(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.cover = note.getCover();
        this.category = note.getCategory();
        this.createdAt = note.getCreatedAt();
        this.icon = note.getIcon();
        this.lastUpdate = note.getLastUpdate();
        if(note.getDescription().length() > 185)
            this.description = note.getDescription().substring(0, 185);
        else
            this.description = note.getDescription();
    }
}
