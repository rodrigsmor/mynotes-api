package com.rm.mynotes.model;

import jakarta.persistence.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CollectionNotes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;
    private String coverUrl;
    private Boolean isPinned = false;
    private Boolean isFavorite = false;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Annotation> annotations = new ArrayList<>();
}
