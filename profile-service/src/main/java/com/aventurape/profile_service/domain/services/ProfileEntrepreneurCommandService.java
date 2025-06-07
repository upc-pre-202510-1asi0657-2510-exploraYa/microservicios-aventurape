package com.aventurape.profile_service.domain.services;

import com.aventurape.profile_service.domain.model.aggregates.ProfileEntrepreneur;
import com.aventurape.profile_service.domain.model.commands.CreateProfileEntrepreneurCommand;

import java.util.Optional;

public interface ProfileEntrepreneurCommandService {
    Optional<ProfileEntrepreneur> handle(CreateProfileEntrepreneurCommand command);
    //Optional<ProfileEntrepreneur> handle(CreateProfileEntrepreneurCommand command);
}
