package com.aventurape.post_service.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record EntrepreneurId(Long entrepreneurId) {
    public EntrepreneurId {
        if (entrepreneurId == null || entrepreneurId <= 0) {
            throw new IllegalArgumentException("Entrepreneur ID cannot be null or negative");
        }
    }
} 