package com.aventurape.post_service.domain.services;

import com.aventurape.post_service.domain.model.aggregates.Publication;
import com.aventurape.post_service.domain.model.commands.CreatePublicationCommand;
import com.aventurape.post_service.domain.model.commands.UpdatePublicationCommand;

import java.util.Optional;

public interface PublicationCommandService {
    Optional<Publication> handle(CreatePublicationCommand command);
    Optional<Publication> handle(UpdatePublicationCommand command);
    boolean handle(Long publicationId);
} 