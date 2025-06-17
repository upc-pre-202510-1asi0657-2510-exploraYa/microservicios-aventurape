package com.aventurape.favorites_service.interfaces.rest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.aventurape.favorites_service.infrastructure.security.jwt.JwtUserDetails;

import com.aventurape.favorites_service.domain.model.commands.DeleteFavoriteCommand;
import com.aventurape.favorites_service.domain.model.queries.GetAllFavoritePublicationsQuery;
import com.aventurape.favorites_service.domain.model.queries.GetFavoritePublicationByProfileIdQuery;
import com.aventurape.favorites_service.domain.model.valueobjects.ProfileId;
import com.aventurape.favorites_service.domain.services.FavoritePublicationCommandService;
import com.aventurape.favorites_service.domain.services.FavoritePublicationQueryService;
import com.aventurape.favorites_service.interfaces.rest.resources.CreateFavoritePublicationResource;
import com.aventurape.favorites_service.interfaces.rest.resources.FavoriteResource;
import com.aventurape.favorites_service.interfaces.rest.transform.CreateFavoritePublicationCommandFromResourceAssembler;
import com.aventurape.favorites_service.interfaces.rest.transform.FavoritePublicationResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/favorites", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Favorite Publications")
public class FavoritePublicationController {

    private final FavoritePublicationCommandService favoriteCommandService;
    private final FavoritePublicationQueryService favoriteQueryService;

    public FavoritePublicationController(FavoritePublicationCommandService favoriteCommandService,
                                         FavoritePublicationQueryService favoriteQueryService) {
        this.favoriteCommandService = favoriteCommandService;
        this.favoriteQueryService = favoriteQueryService;
    }

    //Funcion para el getCurrentUserId
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof JwtUserDetails) {
            return ((JwtUserDetails) authentication.getPrincipal()).getId();
        }
        return null;
    }

    @PostMapping("/create-favorite-publication")
    public ResponseEntity<FavoriteResource> createFavoritePublication(@RequestBody CreateFavoritePublicationResource resource) {
        Long profileId = getCurrentUserId();
        if (profileId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var createFavoritePublicationCommand = CreateFavoritePublicationCommandFromResourceAssembler.toCommandFromResource(resource, profileId);
        var favoritePublication = favoriteCommandService.handle(createFavoritePublicationCommand);
        var favoriteResource = FavoritePublicationResourceFromEntityAssembler.toResourceFromEntity(favoritePublication);
        return new ResponseEntity<>(favoriteResource, HttpStatus.CREATED);
    }

    @GetMapping("/getAllFavoritesPublications")
    public ResponseEntity<List<FavoriteResource>> getAllFavoritePublications() {
        var getAllFavoritePublicationsQuery = new GetAllFavoritePublicationsQuery();
        var favoritePublications = favoriteQueryService.handle(getAllFavoritePublicationsQuery);
        var publicationResources = favoritePublications.stream()
                .map(FavoritePublicationResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return new ResponseEntity<>(publicationResources, HttpStatus.OK);
    }

    @GetMapping("/getFavoritePublicationByProfileId/{profileId}")
    public ResponseEntity<List<FavoriteResource>> getFavoritePublicationByProfileId(@PathVariable Long profileId) {
        if (profileId == null) {
            return ResponseEntity.badRequest().build();
        }
        var profileIdLong=new ProfileId(profileId);
        var getFavoritePublicationByProfileIdQuery = new GetFavoritePublicationByProfileIdQuery(profileIdLong);
        var favoritePublications = favoriteQueryService.handle(getFavoritePublicationByProfileIdQuery);
        var publicationResources = favoritePublications.stream()
                .map(FavoritePublicationResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return new ResponseEntity<>(publicationResources, HttpStatus.OK);
    }

    @DeleteMapping("/delete-favorite-publication/{id}")
    public ResponseEntity<Void> deleteFavoritePublication(@PathVariable Long id) {
        favoriteCommandService.handle(new DeleteFavoriteCommand(id));
        return ResponseEntity.noContent().build();
    }

}