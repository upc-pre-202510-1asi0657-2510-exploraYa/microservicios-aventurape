package com.aventurape.profile_service.domain.services;

import com.aventurape.profile_service.domain.model.aggregates.Profile;
import com.aventurape.profile_service.domain.model.aggregates.ProfileEntrepreneur;
import com.aventurape.profile_service.domain.model.queries.GetAllProfilesEntrepreneurQuery;
import com.aventurape.profile_service.domain.model.queries.GetProfileEByUserIdQuery;
import com.aventurape.profile_service.domain.model.queries.GetProfileEntrepreneurByEmailQuery;
import com.aventurape.profile_service.domain.model.queries.GetProfileEntrepreneurByIdQuery;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ProfileEntrepreneurQueryService {
    List<ProfileEntrepreneur> handle(GetAllProfilesEntrepreneurQuery query);
    Optional<ProfileEntrepreneur> handle(GetProfileEntrepreneurByIdQuery query);
    Optional<ProfileEntrepreneur> handle(GetProfileEntrepreneurByEmailQuery query);
    Optional<ProfileEntrepreneur> handle(GetProfileEByUserIdQuery query);
}
