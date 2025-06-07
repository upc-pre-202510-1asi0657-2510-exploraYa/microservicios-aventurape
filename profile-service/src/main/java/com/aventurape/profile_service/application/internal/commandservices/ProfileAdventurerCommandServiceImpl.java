package com.aventurape.profile_service.application.internal.commandservices;

import com.aventurape.profile_service.domain.model.aggregates.ProfileAdventurer;
import com.aventurape.profile_service.domain.model.commands.CreateProfileAdventurerCommand;
import com.aventurape.profile_service.domain.model.valueobjects.EmailAddress;
import com.aventurape.profile_service.domain.model.valueobjects.UserId;
import com.aventurape.profile_service.domain.services.ProfileAdventureCommandService;
import com.aventurape.profile_service.infrastructure.persistence.jpa.repositories.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileAdventurerCommandServiceImpl implements ProfileAdventureCommandService {
    private final ProfileRepository profileRepository;

    public ProfileAdventurerCommandServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public ProfileAdventurer handle(CreateProfileAdventurerCommand command) {
        var emailAddress = new EmailAddress(command.email());
        var userId = new UserId(command.userId());
        var profile = new ProfileAdventurer(command);
        profileRepository.save(profile);
        return profile;
    }
}
