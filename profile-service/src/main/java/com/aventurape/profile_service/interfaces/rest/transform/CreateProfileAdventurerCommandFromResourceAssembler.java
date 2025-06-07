package com.aventurape.profile_service.interfaces.rest.transform;

import com.aventurape.profile_service.domain.model.commands.CreateProfileAdventurerCommand;
import com.aventurape.profile_service.interfaces.rest.resources.CreateProfileAdventurerResource;

public class CreateProfileAdventurerCommandFromResourceAssembler{
    public static CreateProfileAdventurerCommand toCommandFromResource(
            CreateProfileAdventurerResource resource){
        return new CreateProfileAdventurerCommand(
                resource.firstName(),
                resource.lastName(),
                resource.email(),
                resource.street(),
                resource.number(),
                resource.city(),
                resource.postalCode(),
                resource.country(),
                resource.gender()
        );
    }
}
