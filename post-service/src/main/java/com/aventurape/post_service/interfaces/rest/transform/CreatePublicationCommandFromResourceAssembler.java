package com.aventurape.post_service.interfaces.rest.transform;

import com.aventurape.post_service.domain.model.commands.CreatePublicationCommand;
import com.aventurape.post_service.interfaces.rest.resources.CreatePublicationResource;

public class CreatePublicationCommandFromResourceAssembler {
    
    public static CreatePublicationCommand toCommandFromResource(CreatePublicationResource resource) {
        return new CreatePublicationCommand(
                resource.entrepreneurId(),
                resource.image(),
                resource.cost(),
                resource.nameActivity(),
                resource.description(),
                resource.timeDuration(),
                resource.cantPeople()
        );
    }
} 