package com.aventurape.profile_service.domain.services;

import com.aventurape.profile_service.domain.model.aggregates.Profile;
import com.aventurape.profile_service.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface ProfileQueryService {
    List<Profile> handle(GetProfileByCategoryQuery query);
    Optional<Profile> handle(GetProfileByIdQuery query);

    List<Profile> handle(GetProfileByLocationQuery query);

    Optional<Profile> handle(GetProfileByNameQuery query);
    List<Profile> handle(GetAllProfilesQuery query);
    Optional<Profile> handle(GetProfileAByUserIdQuery query);

}
