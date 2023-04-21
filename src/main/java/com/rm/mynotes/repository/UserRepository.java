package com.rm.mynotes.repository;

import com.rm.mynotes.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByEmail(String email);
    Optional<UserEntity> findByEmail(String email);
    UserEntity getReferenceByEmail(String email);
}
