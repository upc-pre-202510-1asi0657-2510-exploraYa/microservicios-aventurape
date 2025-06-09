package com.aventurape.subscriptions_service.domain.model.commands;

/**
 * Command to subscribe a user to a category
 */
public record SubscribeToCategoryCommand(
    Long userId,
    Long categoryId
) {
} 