package com.aventurape.favorites_service.interfaces.acl.transform;

import com.aventurape.favorites_service.domain.model.valueobjects.PublicationId;

public class PublicationIdDtoAssembler {
    /**
     * Convert a PublicationId to a PublicationIdDto
     * @param publicationId the publication ID to convert
     * @return the DTO
     */
    public static PublicationIdDto toDto(PublicationId publicationId) {
        return new PublicationIdDto(publicationId.getId());
    }
}
