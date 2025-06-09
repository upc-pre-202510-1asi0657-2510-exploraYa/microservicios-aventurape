package com.aventurape.post_service.interfaces.rest.transform;

import com.aventurape.post_service.domain.model.commands.UpdatePublicationCommand;
import com.aventurape.post_service.interfaces.rest.resources.UpdatePublicationResource;

public class UpdatePublicationCommandFromResourceAssembler {
    
    public static UpdatePublicationCommand toCommandFromResource(Long publicationId, UpdatePublicationResource resource) {
        return new UpdatePublicationCommand(
                publicationId,
                resource.image(),
                resource.cost(),
                resource.nameActivity(),
                resource.description(),
                resource.timeDuration(),
                resource.cantPeople()
        );
    }
} 