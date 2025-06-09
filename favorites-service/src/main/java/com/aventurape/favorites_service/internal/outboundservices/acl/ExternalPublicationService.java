package com.aventurape.favorites_service.internal.outboundservices.acl;


import com.upc.aventurape.platform.favorite.interfaces.acl.transform.PublicationIdDto;
import com.upc.aventurape.platform.publication.domain.services.PublicationQueryService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExternalPublicationService {

    private final PublicationQueryService publicationQueryService;

    public ExternalPublicationService(PublicationQueryService publicationQueryService) {
        this.publicationQueryService = publicationQueryService;
    }

    public Optional<PublicationIdDto> fetchPublicationById(Long publicationId) {
        return publicationQueryService.findById(publicationId)
                .map(publication -> new PublicationIdDto(publication.getId()));
    }

    public boolean existsPublicationById(Long publicationId) {
        return publicationQueryService.findById(publicationId).isPresent();
    }
}