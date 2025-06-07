package com.aventurape.profile_service.application.internal.queryservices;

import com.aventurape.profile_service.domain.model.aggregates.ProfileAdventurer;
import com.aventurape.profile_service.domain.model.queries.GetAllProfilesAdventurerQuery;
import com.aventurape.profile_service.domain.model.queries.GetProfileAByUserIdQuery;
import com.aventurape.profile_service.domain.model.queries.GetProfileAdventurerByEmailQuery;
import com.aventurape.profile_service.domain.model.queries.GetProfileAdventurerByIdQuery;
import com.aventurape.profile_service.domain.model.valueobjects.UserId;
import com.aventurape.profile_service.domain.services.ProfileAdventureQueryService;
import com.aventurape.profile_service.infrastructure.persistence.jpa.repositories.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileAdventureQueryServiceImpl implements ProfileAdventureQueryService {
    private final ProfileRepository profileRepository;

    public ProfileAdventureQueryServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public List<ProfileAdventurer> handle(GetAllProfilesAdventurerQuery query) {
        return profileRepository.findAllAdventurers();
    }

    @Override
    public Optional<ProfileAdventurer> handle(GetProfileAdventurerByIdQuery query) {
        return profileRepository.findAdventurerById(query.id());
    }

    @Override
    public Optional<ProfileAdventurer> handle(GetProfileAdventurerByEmailQuery query) {
        return profileRepository.findAdventurerByEmail(query.email());
    }
    @Override
    public Optional<ProfileAdventurer> handle(GetProfileAByUserIdQuery query) {
        return profileRepository.findAdventurerByUserId(new UserId(query.userId()));
    }
}
