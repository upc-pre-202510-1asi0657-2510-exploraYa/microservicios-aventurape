package com.aventurape.subscriptions_service.interfaces.rest.transform;

import com.aventurape.subscriptions_service.domain.model.aggregates.Category;
import com.aventurape.subscriptions_service.interfaces.rest.resources.CategoryResource;

/**
 * Assembler for converting Category to CategoryResource
 */
public class CategoryResourceFromEntityAssembler {
    
    /**
     * Convert a Category to a CategoryResource
     * @param entity the category to convert
     * @return the resource
     */
    public static CategoryResource toResourceFromEntity(Category entity) {
        return new CategoryResource(
                entity.getId(),
                entity.getName(),
                entity.getSubscribersCount()
        );
    }
} 