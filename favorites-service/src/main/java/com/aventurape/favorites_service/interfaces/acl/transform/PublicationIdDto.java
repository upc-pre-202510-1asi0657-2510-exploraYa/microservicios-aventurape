package com.aventurape.favorites_service.interfaces.acl.transform;

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