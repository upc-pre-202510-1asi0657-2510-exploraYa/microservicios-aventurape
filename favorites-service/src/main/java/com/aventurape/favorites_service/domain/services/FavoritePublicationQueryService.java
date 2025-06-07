package com.aventurape.favorites_service.domain.services;


import com.aventurape.favorites_service.domain.model.aggregates.Favorite;
import com.aventurape.favorites_service.domain.model.queries.GetAllFavoritePublicationsQuery;
import com.aventurape.favorites_service.domain.model.queries.GetFavoritePublicationByProfileIdQuery;

import java.util.List;

public interface FavoritePublicationQueryService {
    List<Favorite> handle(GetAllFavoritePublicationsQuery query);
    List<Favorite> handle(GetFavoritePublicationByProfileIdQuery query);
}
