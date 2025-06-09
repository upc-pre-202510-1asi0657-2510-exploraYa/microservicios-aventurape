package com.aventurape.subscriptions_service.domain.model.queries;

/**
 * Query to get popular categories based on subscriber count
 */
public record GetPopularCategoriesQuery(
    Integer limit
) {
    public GetPopularCategoriesQuery() {
        this(10); // Default limit to 10 categories
    }
} 