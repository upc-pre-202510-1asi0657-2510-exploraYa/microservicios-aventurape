package com.aventurape.profile_service.interfaces.rest.transform;

import com.aventurape.profile_service.domain.model.commands.CreateProfileEntrepreneurCommand;
import com.aventurape.profile_service.interfaces.rest.resources.CreateProfileEntrepreneurResource;

public class CreateProfileEntrepreneurCommandFromResourceAssembler {
    public static CreateProfileEntrepreneurCommand toCommandFromResource(CreateProfileEntrepreneurResource resource) {
        return new CreateProfileEntrepreneurCommand(
                resource.userId(),
                resource.emailAddress(),
                resource.addressStreet(),
                resource.addressNumber(),
                resource.addressCity(),
                resource.addressPostalCode(),
                resource.addressCountry(),
                resource.nameEntrepreneurship()
        );
    }
}