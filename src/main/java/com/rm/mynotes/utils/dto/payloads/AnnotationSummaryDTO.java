package com.rm.mynotes.utils.dto.payloads;

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
    private String Cover;
    private OffsetDateTime lastUpdate;
    private OffsetDateTime createdAt;
}
