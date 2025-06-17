package com.aventurape.favorites_service.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record PublicationId(Long publicationId) {
    public PublicationId {
        if (publicationId < 0) {
            throw new IllegalArgumentException("Publication id cannot be negative");
        }
    }

    public PublicationId() {
        this(0L);
    }

    public Long getId() {
        return publicationId;
    }

    public Long value() {
        return publicationId;
    }
}
