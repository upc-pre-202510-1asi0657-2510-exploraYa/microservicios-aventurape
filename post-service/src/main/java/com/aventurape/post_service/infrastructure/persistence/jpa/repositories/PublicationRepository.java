package com.aventurape.post_service.infrastructure.persistence.jpa.repositories;

import com.aventurape.post_service.domain.model.aggregates.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {
    
    Optional<Publication> findById(Long id);
    List<Publication> findByEntrepreneurId(Long entrepreneurId);
    boolean existsById(Long id);
} 