package com.aventurape.favorites_service.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record UserId(Long userId) {
    public UserId {
        if (userId < 0) {
            throw new IllegalArgumentException("User id cannot be negative");
        }
    }

    public UserId() {
        this(0L);
    }
}