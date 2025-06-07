package com.aventurape.post_service.interfaces.rest;

import com.aventurape.post_service.domain.model.queries.GetAllPublicationsQuery;
import com.aventurape.post_service.domain.model.queries.GetPublicationByIdQuery;
import com.aventurape.post_service.domain.model.queries.GetPublicationsByEntrepreneurIdQuery;
import com.aventurape.post_service.domain.services.PublicationCommandService;
import com.aventurape.post_service.domain.services.PublicationQueryService;
import com.aventurape.post_service.interfaces.rest.resources.CreatePublicationResource;
import com.aventurape.post_service.interfaces.rest.resources.PublicationResource;
import com.aventurape.post_service.interfaces.rest.resources.UpdatePublicationResource;
import com.aventurape.post_service.interfaces.rest.transform.CreatePublicationCommandFromResourceAssembler;
import com.aventurape.post_service.interfaces.rest.transform.PublicationResourceFromEntityAssembler;
import com.aventurape.post_service.interfaces.rest.transform.UpdatePublicationCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/publications")
@Tag(name = "Publications", description = "Publication Management Endpoints")
public class PublicationsController {

    private final PublicationQueryService publicationQueryService;
    private final PublicationCommandService publicationCommandService;

    public PublicationsController(PublicationQueryService publicationQueryService, 
                                PublicationCommandService publicationCommandService) {
        this.publicationQueryService = publicationQueryService;
        this.publicationCommandService = publicationCommandService;
    }

    @GetMapping("/all-publications")
    public ResponseEntity<List<PublicationResource>> getAllPublications() {
        var getAllPublicationsQuery = new GetAllPublicationsQuery();
        var publications = publicationQueryService.handle(getAllPublicationsQuery);
        var publicationResources = publications.stream()
                .map(PublicationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(publicationResources);
    }

    @GetMapping("/{publicationId}")
    public ResponseEntity<PublicationResource> getPublicationById(@PathVariable Long publicationId) {
        var getPublicationByIdQuery = new GetPublicationByIdQuery(publicationId);
        var publication = publicationQueryService.handle(getPublicationByIdQuery);
        if (publication.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var publicationResource = PublicationResourceFromEntityAssembler.toResourceFromEntity(publication.get());
        return ResponseEntity.ok(publicationResource);
    }

    @GetMapping("/entrepreneur/{entrepreneurId}")
    public ResponseEntity<List<PublicationResource>> getPublicationsByEntrepreneurId(@PathVariable Long entrepreneurId) {
        var getPublicationsByEntrepreneurIdQuery = new GetPublicationsByEntrepreneurIdQuery(entrepreneurId);
        var publications = publicationQueryService.handle(getPublicationsByEntrepreneurIdQuery);
        var publicationResources = publications.stream()
                .map(PublicationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(publicationResources);
    }

    @PostMapping("/create-publication")
    public ResponseEntity<PublicationResource> createPublication(@RequestBody CreatePublicationResource resource) {
        var createPublicationCommand = CreatePublicationCommandFromResourceAssembler.toCommandFromResource(resource);
        var publication = publicationCommandService.handle(createPublicationCommand);
        if (publication.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var publicationResource = PublicationResourceFromEntityAssembler.toResourceFromEntity(publication.get());
        return new ResponseEntity<>(publicationResource, HttpStatus.CREATED);
    }

    @PutMapping("/update/{publicationId}")
    public ResponseEntity<PublicationResource> updatePublication(@PathVariable Long publicationId,
                                                                @RequestBody UpdatePublicationResource resource) {
        var updatePublicationCommand = UpdatePublicationCommandFromResourceAssembler.toCommandFromResource(publicationId, resource);
        var publication = publicationCommandService.handle(updatePublicationCommand);
        if (publication.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var publicationResource = PublicationResourceFromEntityAssembler.toResourceFromEntity(publication.get());
        return ResponseEntity.ok(publicationResource);
    }

    @DeleteMapping("/delete/{publicationId}")
    public ResponseEntity<Void> deletePublication(@PathVariable Long publicationId) {
        var success = publicationCommandService.handle(publicationId);
        if (success) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
} 