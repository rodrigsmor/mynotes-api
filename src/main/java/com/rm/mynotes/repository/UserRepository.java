package com.rm.mynotes.repository;

import com.rm.mynotes.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByEmail(String email);
    Optional<UserEntity> findByEmail(String email);
    UserEntity getReferenceByEmail(String email);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM user_notes WHERE user_id = :userId AND note_id = :noteId ;", nativeQuery = true)
    Long getNoteBelongsToUser(@Param("userId") Long userId, @Param("noteId") Long noteId);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM user_collections WHERE user_id = :userId AND collection_id = :collectionId ;", nativeQuery = true)
    Long getCollectionBelongsToUser(@Param("userId") Long userId, @Param("collectionId") Long collectionId);
}
