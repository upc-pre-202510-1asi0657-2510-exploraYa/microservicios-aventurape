package com.aventurape.profile_service.domain.services;

import com.aventurape.profile_service.domain.model.aggregates.ProfileAdventurer;
import com.aventurape.profile_service.domain.model.commands.CreateProfileAdventurerCommand;

import java.util.Optional;

public interface ProfileAdventureCommandService {
    //Optional<ProfileAdventurer> handle(CreateProfileAdventurerCommand command);
    ProfileAdventurer handle(CreateProfileAdventurerCommand command);
}
