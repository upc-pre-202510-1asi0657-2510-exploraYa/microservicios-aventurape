package com.aventurape.profile_service.application.internal.commandservices;

import com.aventurape.profile_service.domain.model.aggregates.ProfileEntrepreneur;
import com.aventurape.profile_service.domain.model.commands.CreateProfileEntrepreneurCommand;
import com.aventurape.profile_service.domain.services.ProfileEntrepreneurCommandService;
import com.aventurape.profile_service.infrastructure.persistence.jpa.repositories.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileEntrepreneurCommandServiceImpl implements ProfileEntrepreneurCommandService {

    private final ProfileRepository profileRepository;

    public ProfileEntrepreneurCommandServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Optional<ProfileEntrepreneur> handle(CreateProfileEntrepreneurCommand command) {
        var profile = new ProfileEntrepreneur(command);
        profileRepository.save(profile);
        return Optional.of(profile);
    }
}