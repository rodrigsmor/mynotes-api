package com.rm.mynotes.utils.dto.payloads;

import com.rm.mynotes.model.Annotation;
import com.rm.mynotes.utils.constants.Category;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class AnnotationSummaryDTO {
    private Long id;
    private String title;
    private String description;
    private Category category;
    private String thumbnail;
    private String cover;
    private OffsetDateTime lastUpdate;

    public AnnotationSummaryDTO(Annotation annotation) {
        this.id = annotation.getId();
        this.title = annotation.getTitle();
        this.cover = annotation.getCover();
        this.category = annotation.getCategory();
        this.thumbnail = annotation.getThumbnail();
        this.lastUpdate = annotation.getLastUpdate();
        this.description = annotation.getDescription().substring(0, 185);
    }
}
