package com.rm.mynotes.model;

import com.rm.mynotes.utils.constants.CategoryTypes;
import com.rm.mynotes.utils.dto.requests.AnnotationDTO;
import com.rm.mynotes.utils.functions.CommonFunctions;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notes")
public class Annotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @NotEmpty
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private CategoryTypes category;

    private String icon;

    private String cover;

    @NotNull
    private OffsetDateTime lastUpdate;

    @NotNull
    private OffsetDateTime createdAt;

    private Boolean isExcluded = false;

    private OffsetDateTime deletionDate;

    public Annotation(AnnotationDTO annotationDTO) {
        this.title = annotationDTO.getTitle();
        this.category = annotationDTO.getCategory();
        this.description = annotationDTO.getDescription();
        this.createdAt = CommonFunctions.getCurrentDatetime();
        this.lastUpdate = CommonFunctions.getCurrentDatetime();
    }
}
