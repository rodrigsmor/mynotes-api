package com.rm.mynotes.utils.dto.payloads;

import com.rm.mynotes.model.Note;
import com.rm.mynotes.utils.constants.CategoryTypes;
import com.rm.mynotes.utils.functions.AnnotationMethods;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteDetailedDTO {
    private Long id;
    private String title;
    private String description;
    private CategoryTypes category;
    private String icon;
    private String cover;
    private OffsetDateTime lastUpdate;
    private OffsetDateTime createdAt;
    private List<CollectionSummaryDTO> annotationCollections = new ArrayList<>();

    public NoteDetailedDTO(Note note) {
        AnnotationMethods annotationMethods = new AnnotationMethods();

        this.id = note.getId();
        this.icon = note.getIcon();
        this.cover = note.getCover();
        this.title = note.getTitle();
        this.category = note.getCategory();
        this.createdAt = note.getCreatedAt();
        this.lastUpdate = note.getLastUpdate();
        this.description = note.getDescription();
    }
}
