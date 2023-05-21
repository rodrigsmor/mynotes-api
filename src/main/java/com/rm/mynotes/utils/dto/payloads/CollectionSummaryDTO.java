package com.rm.mynotes.utils.dto.payloads;

import com.rm.mynotes.model.CollectionNotes;
import com.rm.mynotes.repository.CollectionRepository;
import com.rm.mynotes.utils.constants.CategoryTypes;
import com.rm.mynotes.utils.functions.CollectionMethods;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
public class CollectionSummaryDTO {
    private Long id;
    private String name;
    private String coverUrl;
    private Integer numberOfNotes;
    private CategoryTypes category;
    private Boolean isPinned = false;
    private Boolean isFavorite = false;

    public CollectionSummaryDTO(CollectionNotes collection) {
        this.id = collection.getId();
        this.name = collection.getName();
        this.coverUrl = collection.getCoverUrl();
        this.category = collection.getCategory();
        this.isPinned = collection.getIsPinned();
        this.isFavorite = collection.getIsFavorite();
        this.numberOfNotes = 0;
    }
}
