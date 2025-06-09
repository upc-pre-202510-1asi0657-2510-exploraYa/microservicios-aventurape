package com.aventurape.subscriptions_service.interfaces.rest.resources;

/**
 * Resource for category assignment requests
 */
public class AssignCategoryRequest {
    private Long categoryId;
    
    public AssignCategoryRequest() {
    }
    
    public AssignCategoryRequest(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    public Long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
} 