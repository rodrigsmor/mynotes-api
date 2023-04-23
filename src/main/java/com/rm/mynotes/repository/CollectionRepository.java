package com.rm.mynotes.repository;

import com.rm.mynotes.model.CollectionNotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionRepository extends JpaRepository<CollectionNotes, Long> {
    @Query(value = "SELECT EXISTS ( SELECT * FROM collection_notes WHERE note_id = ? AND collection_id = ? );", nativeQuery = true)
    Boolean existsOnCollection(Long noteId, Long collectionId);

    @Query(value = "SELECT COUNT(*) FROM collection_notes WHERE collection_id = ?;", nativeQuery = true)
    Integer getAmountOfAnnotationsInCollection(Long collectionId);

    @Query(value = "SELECT * FROM collections INNER JOIN collection_notes ON collection_notes.collection_id = collections.id WHERE collection_notes.note_id = ?;", nativeQuery = true)
    List<CollectionNotes> getCollectionsByAnnotation(Long annotationId);

    CollectionNotes getReferencedById(Long id);
}
