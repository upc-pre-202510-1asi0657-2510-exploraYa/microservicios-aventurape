package com.aventurape.subscriptions_service.infrastructure.persistence.jpa.repositories;

import com.aventurape.subscriptions_service.domain.model.entities.CategorySubscription;
import com.aventurape.subscriptions_service.domain.model.valueobjects.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategorySubscriptionRepository extends JpaRepository<CategorySubscription, Long> {
    List<CategorySubscription> findByUserId(UserId userId);
    Optional<CategorySubscription> findByUserIdAndCategoryId(UserId userId, Long categoryId);
} 