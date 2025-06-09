package com.aventurape.subscriptions_service.infrastructure.persistence.jpa.repositories;

import com.aventurape.subscriptions_service.domain.model.entities.PublicationCategory;
import com.aventurape.subscriptions_service.domain.model.valueobjects.PublicationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationCategoryRepository extends JpaRepository<PublicationCategory, Long> {
    List<PublicationCategory> findByPublicationId(PublicationId publicationId);
    List<PublicationCategory> findByCategoryId(Long categoryId);
} 