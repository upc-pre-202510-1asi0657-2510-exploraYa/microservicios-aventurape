package com.aventurape.post_service.application.internal.queryservices;

import com.aventurape.post_service.domain.model.aggregates.Publication;
import com.aventurape.post_service.domain.model.queries.GetAllPublicationsQuery;
import com.aventurape.post_service.domain.model.queries.GetPublicationByIdQuery;
import com.aventurape.post_service.domain.model.queries.GetPublicationsByEntrepreneurIdQuery;
import com.aventurape.post_service.domain.services.PublicationQueryService;
import com.aventurape.post_service.infrastructure.persistence.jpa.repositories.PublicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PublicationQueryServiceImpl implements PublicationQueryService {

    private final PublicationRepository publicationRepository;

    public PublicationQueryServiceImpl(PublicationRepository publicationRepository) {
        this.publicationRepository = publicationRepository;
    }

    @Override
    public List<Publication> handle(GetAllPublicationsQuery query) {
        return publicationRepository.findAll();
    }

    @Override
    public Optional<Publication> handle(GetPublicationByIdQuery query) {
        return publicationRepository.findById(query.publicationId());
    }

    @Override
    public List<Publication> handle(GetPublicationsByEntrepreneurIdQuery query) {
        return publicationRepository.findByEntrepreneurId(query.entrepreneurId());
    }
} 