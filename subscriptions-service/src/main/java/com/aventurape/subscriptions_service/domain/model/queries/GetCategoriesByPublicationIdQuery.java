package com.aventurape.subscriptions_service.domain.model.queries;

/**
 * Query to get categories assigned to a publication
 */
public record GetCategoriesByPublicationIdQuery(
    Long publicationId
) {
} 