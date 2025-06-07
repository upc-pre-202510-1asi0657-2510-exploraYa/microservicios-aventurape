package com.aventurape.profile_service.domain.services;

import com.aventurape.profile_service.domain.model.aggregates.ProfileAdventurer;
import com.aventurape.profile_service.domain.model.queries.GetAllProfilesAdventurerQuery;
import com.aventurape.profile_service.domain.model.queries.GetProfileAByUserIdQuery;
import com.aventurape.profile_service.domain.model.queries.GetProfileAdventurerByEmailQuery;
import com.aventurape.profile_service.domain.model.queries.GetProfileAdventurerByIdQuery;

import java.util.List;
import java.util.Optional;

public interface ProfileAdventureQueryService {
    List<ProfileAdventurer> handle(GetAllProfilesAdventurerQuery query);
    Optional<ProfileAdventurer> handle(GetProfileAdventurerByIdQuery query);
    Optional<ProfileAdventurer> handle(GetProfileAdventurerByEmailQuery query);
    Optional<ProfileAdventurer> handle(GetProfileAByUserIdQuery query);
}
