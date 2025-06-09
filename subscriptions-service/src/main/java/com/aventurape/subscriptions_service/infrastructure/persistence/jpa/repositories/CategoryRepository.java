package com.aventurape.subscriptions_service.infrastructure.persistence.jpa.repositories;

import com.aventurape.subscriptions_service.domain.model.aggregates.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
} 