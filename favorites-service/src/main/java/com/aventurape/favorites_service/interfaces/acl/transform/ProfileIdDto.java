package com.aventurape.favorites_service.interfaces.acl.transform;

public class ProfileIdDto {
    private Long profileId;

    public ProfileIdDto() {
    }

    public ProfileIdDto(Long profileId) {
        this.profileId = profileId;
    }

    public Long getProfileId() {
        return profileId;
    }
}
