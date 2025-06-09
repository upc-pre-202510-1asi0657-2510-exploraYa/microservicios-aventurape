package com.aventurape.subscriptions_service.domain.services;

import com.aventurape.subscriptions_service.domain.model.aggregates.Category;
import com.aventurape.subscriptions_service.domain.model.queries.*;
import com.aventurape.subscriptions_service.domain.model.valueobjects.PublicationId;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for handling category queries
 */
public interface CategoryQueryService {

    /**
     * Get all categories
     * @param query the query
     * @return list of all categories
     */
    List<Category> handle(GetAllCategoriesQuery query);

    /**
     * Get a category by ID
     * @param query the query with category ID
     * @return the category if found
     */
    Optional<Category> handle(GetCategoryByIdQuery query);

    /**
     * Get categories subscribed by a user
     * @param query the query with user ID
     * @return list of subscribed categories
     */
    List<Category> handle(GetSubscribedCategoriesByUserIdQuery query);

    /**
     * Get categories for a publication
     * @param query the query with publication ID
     * @return list of categories
     */
    List<Category> handle(GetCategoriesByPublicationIdQuery query);

    /**
     * Get publication IDs for a category
     * @param query the query with category ID
     * @return list of publication IDs
     */
    List<PublicationId> handle(GetPublicationsByCategoryIdQuery query);

    /**
     * Get popular categories based on subscriber count
     * @param query the query with optional limit
     * @return list of categories ordered by subscriber count
     */
    List<Category> handle(GetPopularCategoriesQuery query);
} 