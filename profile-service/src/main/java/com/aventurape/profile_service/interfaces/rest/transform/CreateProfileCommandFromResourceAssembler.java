package com.aventurape.profile_service.interfaces.rest.transform;

import com.aventurape.profile_service.domain.model.commands.CreateProfileCommand;
import com.aventurape.profile_service.interfaces.rest.resources.CreateProfileResource;

public class CreateProfileCommandFromResourceAssembler {
    public static CreateProfileCommand toCommandFromResource(CreateProfileResource resource) {
        return new CreateProfileCommand(
                resource.name(),
                resource.lastName(),
                resource.birthDate(),
                resource.gender(),
                resource.location(),
                resource.category());
    }
}
