package com.aventurape.subscriptions_service.interfaces.rest.resources;

/**
 * Resource for subscription requests
 */
public class SubscriptionRequest {
    private Long userId;
    private Long categoryId;
    
    public SubscriptionRequest() {
    }
    
    public SubscriptionRequest(Long userId, Long categoryId) {
        this.userId = userId;
        this.categoryId = categoryId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
} 