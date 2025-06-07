package com.aventurape.profile_service.domain.services;

import com.aventurape.profile_service.domain.model.aggregates.Profile;
import com.aventurape.profile_service.domain.model.aggregates.ProfileEntrepreneur;
import com.aventurape.profile_service.domain.model.commands.CreateProfileCommand;
import com.aventurape.profile_service.domain.model.commands.DeleteProfileCommand;
import com.aventurape.profile_service.domain.model.commands.UpdateProfileCommand;

import java.util.Optional;
import java.util.OptionalInt;

public interface ProfileCommandService {
    //Profile handle(CreateProfileCommand command);

    Optional<ProfileEntrepreneur> handle(CreateProfileCommand command);
    Optional<Profile> handle(UpdateProfileCommand command);
    void handle(DeleteProfileCommand command);
}
