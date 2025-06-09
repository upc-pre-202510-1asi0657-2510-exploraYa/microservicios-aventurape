package com.aventurape.subscriptions_service.interfaces.acl.transform;

import com.aventurape.subscriptions_service.domain.model.aggregates.Category;

/**
 * Assembler for converting Category to CategoryDto
 */
public class CategoryDtoAssembler {
    
    /**
     * Convert a Category to a CategoryDto
     * @param category the category to convert
     * @return the DTO
     */
    public static CategoryDto toDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getSubscribersCount()
        );
    }
} 