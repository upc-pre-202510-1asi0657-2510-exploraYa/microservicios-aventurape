package com.aventurape.profile_service.interfaces.rest;

import com.aventurape.profile_service.domain.model.aggregates.ProfileAdventurer;
import com.aventurape.profile_service.domain.model.aggregates.ProfileEntrepreneur;
import com.aventurape.profile_service.domain.model.commands.CreateProfileAdventurerCommand;
import com.aventurape.profile_service.domain.model.commands.CreateProfileEntrepreneurCommand;
import com.aventurape.profile_service.domain.model.queries.*;
import com.aventurape.profile_service.domain.services.ProfileAdventureCommandService;
import com.aventurape.profile_service.domain.services.ProfileAdventureQueryService;
import com.aventurape.profile_service.domain.services.ProfileEntrepreneurCommandService;
import com.aventurape.profile_service.domain.services.ProfileEntrepreneurQueryService;
import com.aventurape.profile_service.interfaces.rest.resources.CreateProfileAdventurerResource;
import com.aventurape.profile_service.interfaces.rest.resources.CreateProfileEntrepreneurResource;
import com.aventurape.profile_service.interfaces.rest.resources.ProfileAdventurerResource;
import com.aventurape.profile_service.interfaces.rest.resources.ProfileEntrepreneurResource;
import com.aventurape.profile_service.interfaces.rest.transform.CreateProfileAdventurerCommandFromResourceAssembler;
import com.aventurape.profile_service.interfaces.rest.transform.CreateProfileEntrepreneurCommandFromResourceAssembler;
import com.aventurape.profile_service.interfaces.rest.transform.ProfileAdventurerResourceFromEntityAssembler;
import com.aventurape.profile_service.interfaces.rest.transform.ProfileEntrepreneurResourceFromEntityAssembler;
import com.aventurape.iam_service.infraestructure.security.SecurityUtils;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "Profile Management Endpoints")
public class ProfilesController {
    private static final Logger logger = LoggerFactory.getLogger(ProfilesController.class);
    
    private final ProfileAdventureCommandService profileAdventureCommandService;
    private final ProfileEntrepreneurCommandService profileEntrepreneurCommandService;
    private final ProfileAdventureQueryService profileAdventureQueryService;
    private final ProfileEntrepreneurQueryService profileEntrepreneurQueryService;

    public ProfilesController(
            ProfileAdventureCommandService profileAdventureCommandService,
            ProfileEntrepreneurCommandService profileEntrepreneurCommandService,
            ProfileAdventureQueryService profileAdventureQueryService,
            ProfileEntrepreneurQueryService profileEntrepreneurQueryService) {
        this.profileAdventureCommandService = profileAdventureCommandService;
        this.profileEntrepreneurCommandService = profileEntrepreneurCommandService;
        this.profileAdventureQueryService = profileAdventureQueryService;
        this.profileEntrepreneurQueryService = profileEntrepreneurQueryService;
    }

    @PostMapping("/adventurer")
    public ResponseEntity<ProfileAdventurerResource> createProfileAdventurer(@RequestBody CreateProfileAdventurerResource createProfileAdventurerResource) {
        try {
            Long userId = SecurityUtils.getCurrentUserId();
            if (userId == null) {
                logger.error("No authenticated user found");
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            
            logger.info("Creating adventurer profile for user: {}", userId);
            var createProfileCommand = CreateProfileAdventurerCommandFromResourceAssembler.toCommandFromResource(createProfileAdventurerResource, userId);
            var profileAdventurer = profileAdventureCommandService.handle(createProfileCommand);

            var profileAdventurerResource = ProfileAdventurerResourceFromEntityAssembler.
                    toResourceFromEntity(profileAdventurer);
            return new ResponseEntity<>(profileAdventurerResource, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating adventurer profile", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/entrepreneur")
    public ResponseEntity<ProfileEntrepreneurResource> createProfileEntrepreneur(@RequestBody CreateProfileEntrepreneurResource createProfileEntrepreneurResource) {
        try {
            Long userId = SecurityUtils.getCurrentUserId();
            if (userId == null) {
                logger.error("No authenticated user found");
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            
            logger.info("Creating entrepreneur profile for user: {}", userId);
            var createProfileCommand = CreateProfileEntrepreneurCommandFromResourceAssembler.toCommandFromResource(createProfileEntrepreneurResource, userId);
            var profileEntrepreneur = profileEntrepreneurCommandService.handle(createProfileCommand);
            
            if (profileEntrepreneur.isEmpty()) {
                logger.error("Failed to create entrepreneur profile");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
            var profileEntrepreneurResource = ProfileEntrepreneurResourceFromEntityAssembler.toResourceFromEntity(profileEntrepreneur.get());
            return new ResponseEntity<>(profileEntrepreneurResource, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating entrepreneur profile", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/adventurer/{profileId}")
    public ResponseEntity<ProfileAdventurerResource> getProfileAdventurerById(@PathVariable Long profileId) {
        var getProfileByIdQuery = new GetProfileAdventurerByIdQuery(profileId);
        var profileAdventurer = profileAdventureQueryService.handle(getProfileByIdQuery);
        if(profileAdventurer.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        var profileAdventurerResource = ProfileAdventurerResourceFromEntityAssembler.toResourceFromEntity( profileAdventurer.get() );
        return new ResponseEntity<>(profileAdventurerResource, HttpStatus.OK);
    }

    @GetMapping("/entrepreneur/{profileId}")
    public ResponseEntity<ProfileEntrepreneurResource> getProfileEntrepreneurById(@PathVariable Long profileId) {
        var getProfileByIdQuery = new GetProfileEntrepreneurByIdQuery(profileId);
        var profileAdventurer = profileEntrepreneurQueryService.handle(getProfileByIdQuery);
        if(profileAdventurer.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        var profileEntrepreneurResource = ProfileEntrepreneurResourceFromEntityAssembler.toResourceFromEntity( profileAdventurer.get() );
        return new ResponseEntity<>(profileEntrepreneurResource, HttpStatus.OK);
    }

    @GetMapping("/adventurer")
    public ResponseEntity<List<ProfileAdventurerResource>> getAllProfileAdventurers() {
        var getAllProfilesAdventurerQuery = new GetAllProfilesAdventurerQuery();
        var profilesAdventurer = profileAdventureQueryService.handle(getAllProfilesAdventurerQuery);
        var profileResources = profilesAdventurer.stream()
                .map(ProfileAdventurerResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return new ResponseEntity<>(profileResources, HttpStatus.OK);
    }

    @GetMapping("/entrepreneur")
    public ResponseEntity<List<ProfileEntrepreneurResource>> getAllProfileEntrepreneurs() {
        var getAllProfilesEntrepreneurQuery = new GetAllProfilesEntrepreneurQuery();
        var profilesEntrepreneur = profileEntrepreneurQueryService.handle(getAllProfilesEntrepreneurQuery);
        var profileResources = profilesEntrepreneur.stream()
                .map(ProfileEntrepreneurResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return new ResponseEntity<>(profileResources, HttpStatus.OK);
    }
    
    @GetMapping("/adventurer/user/{userId}")
    public ResponseEntity<Object> getProfileAByUserId(@PathVariable Long userId) {
        var getProfileAByUserIdQuery = new GetProfileAByUserIdQuery(userId);
        var profile = profileAdventureQueryService.handle(getProfileAByUserIdQuery);
        if (profile.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var profileAdventurerResource = ProfileAdventurerResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return new ResponseEntity<>(profileAdventurerResource, HttpStatus.OK);
    }

    @GetMapping("/entrepreneur/user/{userId}")
    public ResponseEntity<Object> getProfileEByUserId(@PathVariable Long userId) {
        var getProfileEByUserIdQuery = new GetProfileEByUserIdQuery(userId);
        var profile = profileEntrepreneurQueryService.handle(getProfileEByUserIdQuery);
        if (profile.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var profileEntrepreneurResource = ProfileEntrepreneurResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return new ResponseEntity<>(profileEntrepreneurResource, HttpStatus.OK);
    }
}