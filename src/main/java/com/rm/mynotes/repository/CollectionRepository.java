package com.rm.mynotes.repository;

import com.rm.mynotes.model.CollectionNotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionRepository extends JpaRepository<CollectionNotes, Long> {
}
