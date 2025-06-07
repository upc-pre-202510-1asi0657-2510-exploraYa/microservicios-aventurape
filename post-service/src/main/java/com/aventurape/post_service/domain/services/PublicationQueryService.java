package com.aventurape.post_service.domain.services;

import com.aventurape.post_service.domain.model.aggregates.Publication;
import com.aventurape.post_service.domain.model.queries.GetAllPublicationsQuery;
import com.aventurape.post_service.domain.model.queries.GetPublicationByIdQuery;
import com.aventurape.post_service.domain.model.queries.GetPublicationsByEntrepreneurIdQuery;

import java.util.List;
import java.util.Optional;

public interface PublicationQueryService {
    List<Publication> handle(GetAllPublicationsQuery query);
    Optional<Publication> handle(GetPublicationByIdQuery query);
    List<Publication> handle(GetPublicationsByEntrepreneurIdQuery query);
} 