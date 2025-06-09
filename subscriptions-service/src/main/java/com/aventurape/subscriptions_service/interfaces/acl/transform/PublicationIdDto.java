package com.aventurape.subscriptions_service.interfaces.acl.transform;

/**
 * DTO for PublicationId
 * Used for communication with other bounded contexts
 */
public class PublicationIdDto {
    private Long publicationId;
    
    public PublicationIdDto() {
    }
    
    public PublicationIdDto(Long publicationId) {
        this.publicationId = publicationId;
    }
    
    public Long getPublicationId() {
        return publicationId;
    }
} 