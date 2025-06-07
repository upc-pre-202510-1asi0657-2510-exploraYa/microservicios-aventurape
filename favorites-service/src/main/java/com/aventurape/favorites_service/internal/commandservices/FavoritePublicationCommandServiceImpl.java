package com.aventurape.favorites_service.internal.commandservices;

import com.upc.aventurape.platform.favorite.application.internal.outboundservices.acl.ExternalProfileService;
import com.upc.aventurape.platform.favorite.application.internal.outboundservices.acl.ExternalPublicationService;
import com.upc.aventurape.platform.favorite.domain.model.aggregates.Favorite;
import com.upc.aventurape.platform.favorite.domain.model.commands.CreateFavoritePublicationCommand;
import com.upc.aventurape.platform.favorite.domain.model.commands.DeleteFavoriteCommand;
import com.upc.aventurape.platform.favorite.domain.model.valueobjects.ProfileId;
import com.upc.aventurape.platform.favorite.domain.model.valueobjects.PublicationId;
import com.upc.aventurape.platform.favorite.domain.services.FavoritePublicationCommandService;
import com.upc.aventurape.platform.favorite.infrastructure.persistence.jpa.repositories.FavoritePublicationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FavoritePublicationCommandServiceImpl implements FavoritePublicationCommandService {

    private final FavoritePublicationRepository favoritePublicationRepository;
    private final ExternalPublicationService externalPublicationService;
    private final ExternalProfileService externalProfileService;

    public FavoritePublicationCommandServiceImpl(
            FavoritePublicationRepository favoritePublicationRepository,
            ExternalPublicationService externalPublicationService,
            ExternalProfileService externalProfileService) {
        this.favoritePublicationRepository = favoritePublicationRepository;
        this.externalPublicationService = externalPublicationService;
        this.externalProfileService = externalProfileService;
    }

    @Override
    public Favorite handle(CreateFavoritePublicationCommand command) {
        // Validar que la publicación existe
        if (!externalPublicationService.existsPublicationById(command.publicationId().getId())) {
            throw new EntityNotFoundException("La publicación no existe");
        }

        // Validar que el perfil existe
        ProfileId profileId = command.profileId();
        PublicationId publicationIdVO = command.publicationId();
        Favorite favorite = new Favorite(profileId, publicationIdVO);
        return favoritePublicationRepository.save(favorite);
    }

    @Override
    public void handle(DeleteFavoriteCommand command) {
        favoritePublicationRepository.deleteById(command.id());
    }
}
