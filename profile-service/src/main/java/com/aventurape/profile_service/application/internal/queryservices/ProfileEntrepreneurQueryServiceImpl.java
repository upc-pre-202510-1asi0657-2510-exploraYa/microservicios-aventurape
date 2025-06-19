package com.aventurape.profile_service.application.internal.queryservices;

import com.aventurape.profile_service.domain.model.aggregates.ProfileEntrepreneur;
import com.aventurape.profile_service.domain.model.queries.GetAllProfilesEntrepreneurQuery;
import com.aventurape.profile_service.domain.model.queries.GetProfileEByUserIdQuery;
import com.aventurape.profile_service.domain.model.queries.GetProfileEntrepreneurByEmailQuery;
import com.aventurape.profile_service.domain.model.queries.GetProfileEntrepreneurByIdQuery;
import com.aventurape.profile_service.domain.model.valueobjects.UserId;
import com.aventurape.profile_service.domain.services.ProfileEntrepreneurQueryService;
import com.aventurape.profile_service.infrastructure.persistence.jpa.repositories.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileEntrepreneurQueryServiceImpl implements ProfileEntrepreneurQueryService {
    private final ProfileRepository profileRepository;
    public ProfileEntrepreneurQueryServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }


    @Override
    public List<ProfileEntrepreneur> handle(GetAllProfilesEntrepreneurQuery query) {
        return profileRepository.findAllEntrepreneurs();
    }

    @Override
    public Optional<ProfileEntrepreneur> handle(GetProfileEntrepreneurByIdQuery query) {
        return profileRepository.findEntrepreneurById(query.id());
    }

    @Override
    public Optional<ProfileEntrepreneur> handle(GetProfileEntrepreneurByEmailQuery query) {
        return profileRepository.findEntrepreneurByEmail(query.email());
    }

    @Override
    public Optional<ProfileEntrepreneur> handle(GetProfileEByUserIdQuery query) {
        return profileRepository.findEntrepreneurByUserId(new UserId(query.userId()));
    }
}
