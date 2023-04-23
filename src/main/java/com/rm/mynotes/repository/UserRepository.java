package com.rm.mynotes.repository;

import com.rm.mynotes.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByEmail(String email);
    Optional<UserEntity> findByEmail(String email);
    UserEntity getReferenceByEmail(String email);

    @Query(value = "SELECT EXISTS ( SELECT * FROM user_annotations WHERE user_id = ? AND annotation_id = ? );", nativeQuery = true)
    Boolean getAnnotationBelongsToUser(Long userId, Long noteId);
}
