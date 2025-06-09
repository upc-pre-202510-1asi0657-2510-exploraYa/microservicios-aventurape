package com.aventurape.subscriptions_service.interfaces.acl.transform;

import com.aventurape.subscriptions_service.domain.model.valueobjects.PublicationId;

/**
 * Assembler for converting PublicationId to PublicationIdDto
 */
public class PublicationIdDtoAssembler {
    
    /**
     * Convert a PublicationId to a PublicationIdDto
     * @param publicationId the publication ID to convert
     * @return the DTO
     */
    public static PublicationIdDto toDto(PublicationId publicationId) {
        return new PublicationIdDto(publicationId.getPublicationId());
    }
} 