package com.aventurape.subscriptions_service.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

/**
 * Value object representing a User ID
 */
@Embeddable
@Getter
public class UserId {
    private Long userId;
    
    public UserId() {
    }
    
    public UserId(Long userId) {
        this.userId = userId;
    }
} 