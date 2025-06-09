package com.aventurape.subscriptions_service.domain.services;

import com.aventurape.subscriptions_service.domain.model.commands.AssignCategoryToPublicationCommand;
import com.aventurape.subscriptions_service.domain.model.commands.RemoveCategoryFromPublicationCommand;
import com.aventurape.subscriptions_service.domain.model.commands.SubscribeToCategoryCommand;
import com.aventurape.subscriptions_service.domain.model.commands.UnsubscribeFromCategoryCommand;
import com.aventurape.subscriptions_service.domain.model.entities.CategorySubscription;
import com.aventurape.subscriptions_service.domain.model.entities.PublicationCategory;

/**
 * Service interface for handling category commands
 */
public interface CategoryCommandService {
    
    /**
     * Subscribe a user to a category
     * @param command the command with user and category IDs
     * @return the created subscription
     */
    CategorySubscription handle(SubscribeToCategoryCommand command);

    /**
     * Unsubscribe a user from a category
     * @param command the command with user and category IDs
     */
    void handle(UnsubscribeFromCategoryCommand command);

    /**
     * Assign a category to a publication
     * @param command the command with publication and category IDs
     * @return the created PublicationCategory relationship
     */
    PublicationCategory handle(AssignCategoryToPublicationCommand command);

    /**
     * Remove a category from a publication
     * @param command the command with publication and category IDs
     */
    void handle(RemoveCategoryFromPublicationCommand command);
} 