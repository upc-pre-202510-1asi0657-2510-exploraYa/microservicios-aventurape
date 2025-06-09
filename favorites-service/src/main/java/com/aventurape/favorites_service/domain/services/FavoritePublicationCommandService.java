package com.aventurape.favorites_service.domain.services;


import com.aventurape.favorites_service.domain.model.aggregates.Favorite;
import com.aventurape.favorites_service.domain.model.commands.CreateFavoritePublicationCommand;
import com.aventurape.favorites_service.domain.model.commands.DeleteFavoriteCommand;

public interface FavoritePublicationCommandService {
    Favorite handle(CreateFavoritePublicationCommand command);
    void handle(DeleteFavoriteCommand command);
}
