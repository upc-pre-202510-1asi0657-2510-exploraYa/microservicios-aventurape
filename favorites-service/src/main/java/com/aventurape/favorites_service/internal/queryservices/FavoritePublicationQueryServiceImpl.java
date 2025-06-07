package com.aventurape.favorites_service.internal.queryservices;

import com.upc.aventurape.platform.favorite.application.internal.outboundservices.acl.ExternalProfileService;
import com.upc.aventurape.platform.favorite.application.internal.outboundservices.acl.ExternalPublicationService;
import com.upc.aventurape.platform.favorite.domain.model.aggregates.Favorite;
import com.upc.aventurape.platform.favorite.domain.model.queries.GetAllFavoritePublicationsQuery;
import com.upc.aventurape.platform.favorite.domain.model.queries.GetFavoritePublicationByProfileIdQuery;
import com.upc.aventurape.platform.favorite.domain.model.valueobjects.ProfileId;
import com.upc.aventurape.platform.favorite.domain.services.FavoritePublicationQueryService;
import com.upc.aventurape.platform.favorite.infrastructure.persistence.jpa.repositories.FavoritePublicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class FavoritePublicationQueryServiceImpl implements FavoritePublicationQueryService {

    private final FavoritePublicationRepository favoriteRepository;
    private final ExternalProfileService externalProfileService;
    private final ExternalPublicationService externalPublicationService;

    public FavoritePublicationQueryServiceImpl(
            FavoritePublicationRepository favoriteRepository,
            ExternalProfileService externalProfileService,
            ExternalPublicationService externalPublicationService) {
        this.favoriteRepository = favoriteRepository;
        this.externalProfileService = externalProfileService;
        this.externalPublicationService = externalPublicationService;
    }

    @Override
    public List<Favorite> handle(GetAllFavoritePublicationsQuery query) {
        return favoriteRepository.findAll();
    }

    @Override
    public List<Favorite> handle(GetFavoritePublicationByProfileIdQuery query) {
        // Aquí podrías verificar que el perfil existe
        return favoriteRepository.findByProfileId(query.profileId());
    }

    // Método adicional que muestra cómo utilizar los servicios externos
    public List<Favorite> getValidFavorites(Long profileId, String profileType) {
        // Verifica que el perfil existe
        var profileDto = externalProfileService.fetchProfileIdByEmail(profileType, profileType);
        if (profileDto.isEmpty()) {
            return List.of(); // Retorna lista vacía si el perfil no existe
        }

        // Obtiene favoritos y filtra solo publicaciones válidas
        List<Favorite> favorites = favoriteRepository.findByProfileId(new ProfileId(profileId));
        return favorites.stream()
                .filter(favorite -> externalPublicationService
                        .existsPublicationById(favorite.getPublicationId().getId()))
                .collect(Collectors.toList());
    }
}