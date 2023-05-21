package com.rm.mynotes.utils.dto.payloads;

import com.rm.mynotes.model.Annotation;
import com.rm.mynotes.utils.constants.CategoryTypes;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class AnnotationSummaryDTO {
    private Long id;
    private String title;
    private String description;
    private CategoryTypes category;
    private String icon;
    private String cover;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastUpdate;

    public AnnotationSummaryDTO(Annotation annotation) {
        this.id = annotation.getId();
        this.title = annotation.getTitle();
        this.cover = annotation.getCover();
        this.category = annotation.getCategory();
        this.createdAt = annotation.getCreatedAt();
        this.icon = annotation.getIcon();
        this.lastUpdate = annotation.getLastUpdate();
        if(annotation.getDescription().length() > 185)
            this.description = annotation.getDescription().substring(0, 185);
        else
            this.description = annotation.getDescription();
    }
}
