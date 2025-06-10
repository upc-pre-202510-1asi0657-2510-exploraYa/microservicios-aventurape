package com.aventurape.favorites_service.internal.queryservices;

import com.aventurape.favorites_service.domain.model.aggregates.Favorite;
import com.aventurape.favorites_service.domain.model.queries.GetAllFavoritePublicationsQuery;
import com.aventurape.favorites_service.domain.model.queries.GetFavoritePublicationByProfileIdQuery;
import com.aventurape.favorites_service.domain.model.valueobjects.ProfileId;
import com.aventurape.favorites_service.domain.services.FavoritePublicationQueryService;
import com.aventurape.favorites_service.internal.outboundservices.acl.ExternalProfileService;
import com.aventurape.favorites_service.internal.outboundservices.acl.ExternalPublicationService;
import com.aventurape.favorites_service.infrastructure.persistence.jpa.repositories.FavoritePublicationRepository;
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

}