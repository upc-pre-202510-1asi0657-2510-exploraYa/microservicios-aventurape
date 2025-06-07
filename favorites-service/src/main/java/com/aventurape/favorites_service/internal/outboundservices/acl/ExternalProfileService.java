package com.aventurape.favorites_service.internal.outboundservices.acl;


import com.aventurape.favorites_service.interfaces.acl.transform.ProfileIdDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Aca se encuentra el servicio externo que permite obtener el ID del perfil de un usuario
// como podemos ver conectamos nuestro acl con el boundary de profiles
@Service
public class ExternalProfileService {

    private final ProfileAdventureCommandService profileAdventureCommandService;
    private final ProfileEntrepreneurCommandService profileEntrepreneurCommandService;
    private final ProfileAdventureQueryService profileAdventureQueryService;
    private final ProfileEntrepreneurQueryService profileEntrepreneurQueryService;

    public ExternalProfileService(ProfileAdventureCommandService profileAdventureCommandService,
                                  ProfileEntrepreneurCommandService profileEntrepreneurCommandService,
                                  ProfileAdventureQueryService profileAdventureQueryService,
                                  ProfileEntrepreneurQueryService profileEntrepreneurQueryService) {
        this.profileAdventureCommandService = profileAdventureCommandService;
        this.profileEntrepreneurCommandService = profileEntrepreneurCommandService;
        this.profileAdventureQueryService = profileAdventureQueryService;
        this.profileEntrepreneurQueryService = profileEntrepreneurQueryService;
    }

    public Optional<ProfileIdDto> fetchProfileIdByEmail(String email, String profileType) {
        if ("adventurer".equalsIgnoreCase(profileType)) {
            var getProfileByEmailQuery = new GetProfileAdventurerByEmailQuery(email);
            return profileAdventureQueryService.handle(getProfileByEmailQuery)
                    .map(profile -> new ProfileIdDto(profile.getId()));
        } else if ("entrepreneur".equalsIgnoreCase(profileType)) {
            var getProfileByEmailQuery = new GetProfileEntrepreneurByEmailQuery(email);
            return profileEntrepreneurQueryService.handle(getProfileByEmailQuery)
                    .map(profile -> new ProfileIdDto(profile.getId()));
        }
        throw new IllegalArgumentException("Invalid profile type: " + profileType);
    }

}