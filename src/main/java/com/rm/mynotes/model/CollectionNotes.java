package com.rm.mynotes.model;

import com.rm.mynotes.utils.constants.CategoryTypes;
import com.rm.mynotes.utils.dto.requests.CollectionDTO;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "collections")
public class CollectionNotes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    private String coverUrl;
    private Boolean isPinned = false;
    private Boolean isFavorite = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CategoryTypes category;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "collection_notes", joinColumns = @JoinColumn(name = "collection_id"),
        inverseJoinColumns = @JoinColumn(name = "note_id", referencedColumnName = "id")
    )
    private List<Note> notes = new ArrayList<>();

    public CollectionNotes(CollectionDTO collectionDTO) {
        this.isFavorite = false;
        this.name = collectionDTO.getName();
        this.notes = new ArrayList<>();
        this.category = collectionDTO.getCategory();
        this.isPinned = collectionDTO.getIsPinned();
        this.coverUrl = collectionDTO.getCoverUrl();
    }
}
