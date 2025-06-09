package com.aventurape.favorites_service.interfaces.rest.transform;

import com.aventurape.favorites_service.domain.model.commands.CreateFavoritePublicationCommand;
import com.aventurape.favorites_service.domain.model.valueobjects.ProfileId;
import com.aventurape.favorites_service.domain.model.valueobjects.PublicationId;
import com.aventurape.favorites_service.interfaces.rest.resources.CreateFavoritePublicationResource;

public class CreateFavoritePublicationCommandFromResourceAssembler {
    public static CreateFavoritePublicationCommand toCommandFromResource(
            CreateFavoritePublicationResource resource, Long profileId){
        // Creamos el PublicationId directamente con el valor del constructor
        PublicationId publicationId = new PublicationId(resource.publicationId());

        // Retornamos el comando con los value objects
        return new CreateFavoritePublicationCommand(new ProfileId(profileId), publicationId);
    }
}