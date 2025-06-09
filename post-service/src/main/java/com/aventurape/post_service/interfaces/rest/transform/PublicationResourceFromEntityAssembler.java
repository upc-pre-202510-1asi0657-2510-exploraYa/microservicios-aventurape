package com.aventurape.post_service.interfaces.rest.transform;

import com.aventurape.post_service.domain.model.aggregates.Publication;
import com.aventurape.post_service.interfaces.rest.resources.PublicationResource;

public class PublicationResourceFromEntityAssembler {
    
    public static PublicationResource toResourceFromEntity(Publication publication) {
        return new PublicationResource(
                publication.getId(),
                publication.getEntrepreneurId(),
                publication.getNameActivity(),
                publication.getDescription(),
                publication.getTimeDuration(),
                publication.getImage(),
                publication.getCantPeople(),
                publication.getCost()
        );
    }
} 