package com.aventurape.profile_service.domain.model.valueobjects;

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