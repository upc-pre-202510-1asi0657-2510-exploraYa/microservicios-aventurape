package com.aventurape.profile_service.interfaces.rest.transform;

import com.aventurape.profile_service.domain.model.commands.UpdateProfileCommand;
import com.aventurape.profile_service.interfaces.rest.resources.UpdateProfileResource;

public class UpdateProfileCommandFromResourceAssembler {
    public static UpdateProfileCommand toCommandFromResource(Long profileId,
                                                                UpdateProfileResource resource) {
        return new UpdateProfileCommand(
                profileId,
                resource.name(),
                resource.lastName(),
                resource.birthDate(),
                resource.gender(),
                resource.location(),
                resource.category());
    }
}
