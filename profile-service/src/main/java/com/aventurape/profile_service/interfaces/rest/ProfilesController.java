package com.aventurape.profile_service.interfaces.rest;

import com.aventurape.profile_service.domain.model.queries.*;
import com.aventurape.profile_service.domain.services.ProfileAdventureCommandService;
import com.aventurape.profile_service.domain.services.ProfileAdventureQueryService;
import com.aventurape.profile_service.domain.services.ProfileEntrepreneurCommandService;
import com.aventurape.profile_service.domain.services.ProfileEntrepreneurQueryService;
import com.aventurape.profile_service.infrastructure.security.configuration.jwt.JwtTokenUtil;
import com.aventurape.profile_service.infrastructure.security.configuration.jwt.JwtUserDetails;
import com.aventurape.profile_service.interfaces.rest.resources.CreateProfileAdventurerResource;
import com.aventurape.profile_service.interfaces.rest.resources.CreateProfileEntrepreneurResource;
import com.aventurape.profile_service.interfaces.rest.resources.ProfileAdventurerResource;
import com.aventurape.profile_service.interfaces.rest.resources.ProfileEntrepreneurResource;
import com.aventurape.profile_service.interfaces.rest.transform.CreateProfileAdventurerCommandFromResourceAssembler;
import com.aventurape.profile_service.interfaces.rest.transform.CreateProfileEntrepreneurCommandFromResourceAssembler;
import com.aventurape.profile_service.interfaces.rest.transform.ProfileAdventurerResourceFromEntityAssembler;
import com.aventurape.profile_service.interfaces.rest.transform.ProfileEntrepreneurResourceFromEntityAssembler;
//import com.aventurape.iam_service.infraestructure.security.SecurityUtils;
import com.thoughtworks.xstream.core.SecurityUtils;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/adventurer/create-adventurer")
    @PreAuthorize("hasAnyAuthority('ROLE_ADVENTUROUS', 'ROLE_ADMIN')")
    public ResponseEntity<ProfileAdventurerResource> createProfileAdventurer(@RequestBody CreateProfileAdventurerResource resource) {
        logger.debug("Recibida solicitud para crear publicación: {}", resource);
        
        // Obtener autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("Autenticación actual: {}", authentication);
        logger.debug("Autoridades: {}", authentication != null ? authentication.getAuthorities() : "ninguna");

        // Verificar si el usuario es admin
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        logger.debug("¿Es admin? {}", isAdmin);

        //verificar si el usuario es aventurero
        boolean isAdventurer = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADVENTUROUS"));
        logger.debug("¿Es aventurero? {}", isAdventurer);
        if (!isAdmin && !isAdventurer) {
            logger.error("Usuario no autorizado para crear perfil de aventurero");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Long userId = getCurrentUserId();
        if (userId == null) {
            logger.error("No se pudo obtener el ID de usuario actual");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var createProfileCommand = CreateProfileAdventurerCommandFromResourceAssembler.toCommandFromResource(resource, userId);
        var profileAdventurer = profileAdventureCommandService.handle(createProfileCommand);
        if (profileAdventurer == null) {
            logger.error("Error al crear el perfil de aventurero");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        var profileAdventurerResource = ProfileAdventurerResourceFromEntityAssembler.toResourceFromEntity(profileAdventurer);
        logger.info("Perfil de aventurero creado con éxito: {}", profileAdventurerResource);
        return new ResponseEntity<>(profileAdventurerResource, HttpStatus.CREATED);
    }

    @PostMapping("/entrepreneur/create-entrepreneur")
    @PreAuthorize("hasAnyAuthority('ROLE_ENTREPRENEUR', 'ROLE_ADMIN')")
    public ResponseEntity<ProfileEntrepreneurResource> createProfileEntrepreneur(@RequestBody CreateProfileEntrepreneurResource resource) {
        logger.debug("Recibida solicitud para crear perfil de emprendedor: {}", resource);
        
        // Obtener autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("Autenticación actual: {}", authentication);
        logger.debug("Autoridades: {}", authentication != null ? authentication.getAuthorities() : "ninguna");

        // Verificar si el usuario es admin
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        logger.debug("¿Es admin? {}", isAdmin);

        //verificar si el usuario es emprendedor
        boolean isEntrepreneur = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ENTREPRENEUR"));
        logger.debug("¿Es emprendedor? {}", isEntrepreneur);
        if (!isAdmin && !isEntrepreneur) {
            logger.error("Usuario no autorizado para crear perfil de emprendedor");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Long userId = getCurrentUserId();
        if (userId == null) {
            logger.error("No se pudo obtener el ID de usuario actual");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var createProfileCommand = CreateProfileEntrepreneurCommandFromResourceAssembler.toCommandFromResource(resource, userId);
        var profileEntrepreneur = profileEntrepreneurCommandService.handle(createProfileCommand);
        if (profileEntrepreneur == null) {
            logger.error("Error al crear el perfil de emprendedor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        var profileEntrepreneurResource = 
        ProfileEntrepreneurResourceFromEntityAssembler.toResourceFromEntity(profileEntrepreneur.get());
        logger.info("Perfil de emprendedor creado con éxito: {}", profileEntrepreneurResource);
        return new ResponseEntity<>(profileEntrepreneurResource, HttpStatus.CREATED);
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

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof JwtUserDetails) {
            return ((JwtUserDetails) authentication.getPrincipal()).getId();
        }
        return null;
    }
}