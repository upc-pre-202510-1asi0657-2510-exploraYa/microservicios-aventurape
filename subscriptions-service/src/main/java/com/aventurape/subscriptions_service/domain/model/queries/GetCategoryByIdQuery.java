package com.aventurape.subscriptions_service.domain.model.queries;

/**
 * Query to get a category by its ID
 */
public record GetCategoryByIdQuery(
    Long categoryId
) {
} 