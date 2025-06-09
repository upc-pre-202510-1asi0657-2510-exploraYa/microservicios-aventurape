package com.aventurape.subscriptions_service.domain.model.commands;

/**
 * Command to remove a category from a publication
 */
public record RemoveCategoryFromPublicationCommand(
    Long publicationId,
    Long categoryId
) {
} 