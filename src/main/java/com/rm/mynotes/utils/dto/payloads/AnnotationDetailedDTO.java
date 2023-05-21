package com.rm.mynotes.utils.dto.payloads;

import com.rm.mynotes.model.Annotation;
import com.rm.mynotes.utils.constants.CategoryTypes;
import com.rm.mynotes.utils.functions.AnnotationMethods;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnotationDetailedDTO {
    private Long id;
    private String title;
    private String description;
    private CategoryTypes category;
    private String icon;
    private String cover;
    private OffsetDateTime lastUpdate;
    private OffsetDateTime createdAt;
    private List<CollectionSummaryDTO> annotationCollections = new ArrayList<>();

    public AnnotationDetailedDTO(Annotation annotation) {
        AnnotationMethods annotationMethods = new AnnotationMethods();

        this.id = annotation.getId();
        this.icon = annotation.getIcon();
        this.cover = annotation.getCover();
        this.title = annotation.getTitle();
        this.category = annotation.getCategory();
        this.createdAt = annotation.getCreatedAt();
        this.lastUpdate = annotation.getLastUpdate();
        this.description = annotation.getDescription();
        this.annotationCollections = annotationMethods.getCollectionsThatHaveTheAnnotation(annotation.getId());
    }
}
