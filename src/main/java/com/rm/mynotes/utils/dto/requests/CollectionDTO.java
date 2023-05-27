package com.rm.mynotes.utils.dto.requests;

import com.rm.mynotes.model.Note;
import com.rm.mynotes.utils.constants.CategoryTypes;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollectionDTO {
    private String name;
    private String coverUrl;
    private Boolean isPinned;
    private Boolean isFavorite = false;
    private CategoryTypes category;
    private List<Note> notes = new ArrayList<>();
}
