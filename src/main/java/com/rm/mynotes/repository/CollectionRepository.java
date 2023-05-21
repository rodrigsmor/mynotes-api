package com.rm.mynotes.repository;

import com.rm.mynotes.model.CollectionNotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionRepository extends JpaRepository<CollectionNotes, Long> {
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM collection_notes WHERE note_id = :noteId AND collection_id = :collectionId ;", nativeQuery = true)
    Long existsOnCollection(@Param("noteId") Long noteId, @Param("collectionId") Long collectionId);

    @Query(value = "SELECT COUNT(*) FROM collection_notes WHERE collection_id = :collectionId ;", nativeQuery = true)
    Integer getAmountOfAnnotationsInCollection(@Param("collectionId") Long collectionId);

    @Query(value = "SELECT * FROM collections INNER JOIN collection_notes ON collection_notes.collection_id = collections.id WHERE collection_notes.note_id = :noteId ;", nativeQuery = true)
    List<CollectionNotes> getCollectionsByAnnotation(@Param("noteId") Long annotationId);

    CollectionNotes getReferencedById(Long id);
}
