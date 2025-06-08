package com.aventurape.profile_service.interfaces.rest.transform;

import com.aventurape.profile_service.domain.model.aggregates.ProfileAdventurer;
import com.aventurape.profile_service.interfaces.rest.resources.ProfileAdventurerResource;

public class ProfileAdventurerResourceFromEntityAssembler {
    public static ProfileAdventurerResource toResourceFromEntity(ProfileAdventurer entity) {
        return new ProfileAdventurerResource(
                entity.getId(),
                entity.getUserId(),
                entity.getFirstName() + " " + entity.getLastName(),
                entity.getGender(),
                entity.getEmailAddress(),
                entity.getStreetAddress()
        );
    }
}