package com.rm.mynotes.repository;

import com.rm.mynotes.model.Annotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface AnnotationRepository extends JpaRepository<Annotation, Long> {
    @Query(value = "SELECT * FROM notes WHERE notes.deletion_date < :deletionDate ; ", nativeQuery = true)
    List<Annotation> findByDeletionDateBefore(@Param("deletionDate") OffsetDateTime deletionDate);

    @Modifying
    @Query(value = "DELETE FROM collection_notes WHERE note_id = :noteId", nativeQuery = true)
    void removeCollectionNotesRelations(@Param("noteId") Long noteId);

    @Modifying
    @Query(value = "DELETE FROM user_annotations WHERE annotation_id = :noteId", nativeQuery = true)
    void removeUserAnnotationsRelations(@Param("noteId") Long noteId);

    default void removeAllRelationsFromAnnotation(Long noteId) {
        removeCollectionNotesRelations(noteId);
        removeUserAnnotationsRelations(noteId);
    }
}
