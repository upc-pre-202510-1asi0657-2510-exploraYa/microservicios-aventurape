package com.aventurape.profile_service.application.internal.commandservices;

//import com.aventurape.iam_service.infraestructure.authorization.sfs.model.UserDetailsImpl;
//import com.aventurape.iam_service.infraestructure.security.SecurityUtils;
import com.aventurape.profile_service.domain.model.aggregates.Profile;
import com.aventurape.profile_service.domain.model.aggregates.ProfileAdventurer;
import com.aventurape.profile_service.domain.model.commands.CreateProfileAdventurerCommand;
import com.aventurape.profile_service.domain.model.valueobjects.EmailAddress;
import com.aventurape.profile_service.domain.model.valueobjects.UserId;
import com.aventurape.profile_service.domain.services.ProfileAdventureCommandService;
import com.aventurape.profile_service.infrastructure.persistence.jpa.repositories.ProfileRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileAdventurerCommandServiceImpl implements ProfileAdventureCommandService {
    private final ProfileRepository profileRepository;

    public ProfileAdventurerCommandServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public ProfileAdventurer handle(CreateProfileAdventurerCommand command) {
        /*var emailAddress = new EmailAddress(command.email());
        var userId = new UserId(command.userId());
        var profile = new ProfileAdventurer(command);
        profileRepository.save(profile);
        return profile;
        */
        // Check if a profile already exists for the user
        /* 
        List<ProfileAdventurer> existingProfile = profileRepository.findByUserId(userDetails.getId());
        if (!existingProfile.isEmpty()) {
            throw new IllegalArgumentException("A profile already exists for this user");
        }

        List<ProfileAdventurer> profilesByEmail = profileRepository.findByEmail(command.email());

        if (!profilesByEmail.isEmpty()) {
            throw new IllegalArgumentException("Email already exists");
        }
        */
        var profile = new ProfileAdventurer(command);

        profileRepository.save(profile);

        return profile;
    }
}
