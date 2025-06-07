package com.aventurape.favorites_service.interfaces.rest.transform;


import com.upc.aventurape.platform.favorite.domain.model.commands.CreateFavoritePublicationCommand;
import com.upc.aventurape.platform.favorite.domain.model.valueobjects.ProfileId;
import com.upc.aventurape.platform.favorite.domain.model.valueobjects.PublicationId;
import com.upc.aventurape.platform.favorite.interfaces.rest.resources.CreateFavoritePublicationResource;

public class CreateFavoritePublicationCommandFromResourceAssembler {
    public static CreateFavoritePublicationCommand toCommandFromResource(
            CreateFavoritePublicationResource resource, Long profileId){
        // Creamos el PublicationId directamente con el valor del constructor
        PublicationId publicationId = new PublicationId(resource.publicationId());

        // Retornamos el comando con los value objects
        return new CreateFavoritePublicationCommand(new ProfileId(profileId), publicationId);
    }
}