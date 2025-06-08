package com.aventurape.profile_service.interfaces.rest.transform;

import com.aventurape.profile_service.domain.model.aggregates.ProfileEntrepreneur;
import com.aventurape.profile_service.interfaces.rest.resources.ProfileEntrepreneurResource;

public class ProfileEntrepreneurResourceFromEntityAssembler {
    public static ProfileEntrepreneurResource toResourceFromEntity(ProfileEntrepreneur entity) {
        return new ProfileEntrepreneurResource(
                entity.getId(),
                entity.getUserId(),
                entity.getName(),
                entity.getCity(),
                entity.getCountry(),
                entity.getNumber(),
                entity.getPostalCode(),
                entity.getStreetAddress(),
                entity.getEmailAddress()
                );
    }
}