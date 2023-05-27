package com.rm.mynotes.repository;

import com.rm.mynotes.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    @Query(value = "SELECT * FROM notes WHERE notes.deletion_date < :deletionDate ; ", nativeQuery = true)
    List<Note> findByDeletionDateBefore(@Param("deletionDate") OffsetDateTime deletionDate);

    @Modifying
    @Query(value = "DELETE FROM collection_notes WHERE note_id = :noteId", nativeQuery = true)
    void removeCollectionNotesRelations(@Param("noteId") Long noteId);

    @Modifying
    @Query(value = "DELETE FROM user_notes WHERE note_id = :noteId", nativeQuery = true)
    void removeUserNotesRelations(@Param("noteId") Long noteId);

    default void removeAllRelationsFromNote(Long noteId) {
        removeCollectionNotesRelations(noteId);
        removeUserNotesRelations(noteId);
    }
}
