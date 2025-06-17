package com.aventurape.favorites_service.interfaces.acl.transform;

import com.aventurape.favorites_service.domain.model.valueobjects.ProfileId;

public class ProfileIdDtoAssembler {
    /**
     * Convert a ProfileId to a ProfileIdDto
     * @param profileId the profile ID to convert
     * @return the DTO
     */
    public static ProfileIdDto toDto(ProfileId profileId) {
        return new ProfileIdDto(profileId.getId());
    }
}
