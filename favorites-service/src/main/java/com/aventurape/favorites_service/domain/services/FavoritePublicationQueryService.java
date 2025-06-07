package com.aventurape.favorites_service.domain.services;

import com.upc.aventurape.platform.favorite.domain.model.aggregates.Favorite;
import com.upc.aventurape.platform.favorite.domain.model.queries.GetAllFavoritePublicationsQuery;
import com.upc.aventurape.platform.favorite.domain.model.queries.GetFavoritePublicationByProfileIdQuery;

import java.util.List;

public interface FavoritePublicationQueryService {
    List<Favorite> handle(GetAllFavoritePublicationsQuery query);
    List<Favorite> handle(GetFavoritePublicationByProfileIdQuery query);
}
