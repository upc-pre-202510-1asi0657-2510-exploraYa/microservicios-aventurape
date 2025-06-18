package com.aventurape.stats_service.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record EntrepreneurId(Long entrepreneurId) {
    public EntrepreneurId {
        if (entrepreneurId < 0) {
            throw new IllegalArgumentException("Entrepreneur id cannot be negative");
        }
    }

    public EntrepreneurId() {
        this(0L);
    }
} 