package com.aventurape.post_service.application.internal.commandservices;

import com.aventurape.post_service.domain.model.aggregates.Publication;
import com.aventurape.post_service.domain.model.commands.CreatePublicationCommand;
import com.aventurape.post_service.domain.model.commands.UpdatePublicationCommand;
import com.aventurape.post_service.domain.model.entities.Adventure;
import com.aventurape.post_service.domain.services.PublicationCommandService;
import com.aventurape.post_service.infrastructure.persistence.jpa.repositories.PublicationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PublicationCommandServiceImpl implements PublicationCommandService {

    private final PublicationRepository publicationRepository;

    public PublicationCommandServiceImpl(PublicationRepository publicationRepository) {
        this.publicationRepository = publicationRepository;
    }

    @Override
    public Optional<Publication> handle(CreatePublicationCommand command) {
        var adventure = new Adventure(
                command.nameActivity(),
                command.description(),
                command.timeDuration(),
                command.cantPeople()
        );

        var publication = new Publication(
                command.entrepreneurId(),
                command.image(),
                command.cost(),
                adventure
        );

        try {
            publicationRepository.save(publication);
            return Optional.of(publication);
        } catch (Exception e) {
            throw new RuntimeException("Error creating publication: " + e.getMessage());
        }
    }

    @Override
    public Optional<Publication> handle(UpdatePublicationCommand command) {
        return publicationRepository.findById(command.publicationId())
                .map(publication -> {
                    var adventure = publication.getAdventure();
                    adventure.updateInformation(
                            command.nameActivity(),
                            command.description(),
                            command.timeDuration(),
                            command.cantPeople()
                    );

                    publication.updateInformation(
                            command.image(),
                            command.cost(),
                            adventure
                    );

                    publicationRepository.save(publication);
                    return publication;
                });
    }

    @Override
    public boolean handle(Long publicationId) {
        if (publicationRepository.existsById(publicationId)) {
            publicationRepository.deleteById(publicationId);
            return true;
        }
        return false;
    }
} 