package com.aventurape.subscriptions_service.domain.model.commands;

/**
 * Command to unsubscribe a user from a category
 */
public record UnsubscribeFromCategoryCommand(
    Long userId,
    Long categoryId
) {
} 