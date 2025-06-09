package com.aventurape.subscriptions_service.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

/**
 * Value object representing a Publication ID
 */
@Embeddable
@Getter
public class PublicationId {
    private Long publicationId;
    
    public PublicationId() {
    }
    
    public PublicationId(Long publicationId) {
        this.publicationId = publicationId;
    }
} 