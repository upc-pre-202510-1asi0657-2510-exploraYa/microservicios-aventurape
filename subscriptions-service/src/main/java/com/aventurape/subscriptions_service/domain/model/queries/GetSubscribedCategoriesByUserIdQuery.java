package com.aventurape.subscriptions_service.domain.model.queries;

/**
 * Query to get categories subscribed by a user
 */
public record GetSubscribedCategoriesByUserIdQuery(
    Long userId
) {
} 