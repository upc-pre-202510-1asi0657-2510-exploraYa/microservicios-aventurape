package com.aventurape.subscriptions_service.domain.model.queries;

/**
 * Query to get publications assigned to a category
 */
public record GetPublicationsByCategoryIdQuery(
    Long categoryId
) {
} 