package com.aventurape.favorites_service.domain.services;

import com.upc.aventurape.platform.favorite.domain.model.aggregates.Favorite;
import com.upc.aventurape.platform.favorite.domain.model.commands.CreateFavoritePublicationCommand;
import com.upc.aventurape.platform.favorite.domain.model.commands.DeleteFavoriteCommand;

public interface FavoritePublicationCommandService {
    Favorite handle(CreateFavoritePublicationCommand command);
    void handle(DeleteFavoriteCommand command);
}
