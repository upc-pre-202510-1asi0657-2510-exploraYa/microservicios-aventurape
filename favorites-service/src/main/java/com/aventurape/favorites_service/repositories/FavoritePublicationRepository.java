package com.aventurape.favorites_service.repositories;

import com.upc.aventurape.platform.favorite.domain.model.aggregates.Favorite;
import com.upc.aventurape.platform.favorite.domain.model.valueobjects.ProfileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritePublicationRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByProfileId(ProfileId profileId);
}