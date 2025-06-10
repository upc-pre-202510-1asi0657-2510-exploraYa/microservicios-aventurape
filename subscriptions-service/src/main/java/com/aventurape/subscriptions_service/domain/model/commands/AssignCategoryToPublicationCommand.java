package com.aventurape.subscriptions_service.domain.model.commands;

/**
 * Command to assign a category to a publication
 */
public record AssignCategoryToPublicationCommand(
        Long publicationId,
        Long categoryId
) {
}