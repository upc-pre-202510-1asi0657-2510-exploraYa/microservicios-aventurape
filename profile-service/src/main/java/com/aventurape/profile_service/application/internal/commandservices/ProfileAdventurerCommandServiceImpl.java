package com.aventurape.profile_service.application.internal.commandservices;


import com.aventurape.profile_service.domain.model.aggregates.ProfileAdventurer;
import com.aventurape.profile_service.domain.model.commands.CreateProfileAdventurerCommand;
import com.aventurape.profile_service.domain.services.ProfileAdventureCommandService;
import com.aventurape.profile_service.infrastructure.persistence.jpa.repositories.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileAdventurerCommandServiceImpl implements ProfileAdventureCommandService {
    private final ProfileRepository profileRepository;

    public ProfileAdventurerCommandServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public ProfileAdventurer handle(CreateProfileAdventurerCommand command) {
        var profile = new ProfileAdventurer(command);
        profileRepository.save(profile);
        return profile;
    }
}
