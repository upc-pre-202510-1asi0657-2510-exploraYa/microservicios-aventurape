package com.aventurape.post_service.interfaces.rest;

import com.aventurape.post_service.domain.model.queries.GetAllPublicationsQuery;
import com.aventurape.post_service.domain.model.queries.GetPublicationByIdQuery;
import com.aventurape.post_service.domain.model.queries.GetPublicationsByEntrepreneurIdQuery;
import com.aventurape.post_service.domain.services.PublicationCommandService;
import com.aventurape.post_service.domain.services.PublicationQueryService;
import com.aventurape.post_service.infrastructure.security.jwt.JwtUserDetails;
import com.aventurape.post_service.interfaces.rest.resources.CreatePublicationResource;
import com.aventurape.post_service.interfaces.rest.resources.PublicationResource;
import com.aventurape.post_service.interfaces.rest.resources.UpdatePublicationResource;
import com.aventurape.post_service.interfaces.rest.transform.CreatePublicationCommandFromResourceAssembler;
import com.aventurape.post_service.interfaces.rest.transform.PublicationResourceFromEntityAssembler;
import com.aventurape.post_service.interfaces.rest.transform.UpdatePublicationCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/publications")
@Tag(name = "Publications", description = "Publication Management Endpoints")
public class PublicationsController {

    private static final Logger logger = LoggerFactory.getLogger(PublicationsController.class);
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
    @PreAuthorize("hasAnyAuthority('ROLE_ENTREPRENEUR', 'ROLE_ADMIN')")
    public ResponseEntity<PublicationResource> createPublication(@RequestBody CreatePublicationResource resource) {
        logger.debug("Recibida solicitud para crear publicación: {}", resource);
        
        // Obtener autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("Autenticación actual: {}", authentication);
        logger.debug("Autoridades: {}", authentication != null ? authentication.getAuthorities() : "ninguna");
        
        // Verificar si el usuario es admin
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        // Si no es admin, verificar que el ID coincida con el emprendedor
        if (!isAdmin) {
            // Obtener el ID del usuario actual del token JWT
            Long currentUserId = getCurrentUserId();
            logger.debug("ID de usuario actual: {}", currentUserId);
    
            // Asegurarse de que el ID del emprendedor en la publicación coincida con el ID del usuario autenticado
            if (currentUserId == null || !currentUserId.equals(resource.entrepreneurId())) {
                logger.debug("Acceso prohibido: el ID del usuario ({}) no coincide con el ID del emprendedor ({})", 
                        currentUserId, resource.entrepreneurId());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            logger.debug("Usuario admin creando publicación para emprendedor ID: {}", resource.entrepreneurId());
        }

        var createPublicationCommand = CreatePublicationCommandFromResourceAssembler.toCommandFromResource(resource);
        var publicationOptional = publicationCommandService.handle(createPublicationCommand);

        if (publicationOptional.isEmpty()) {
            logger.debug("La publicación no pudo ser creada");
            return ResponseEntity.badRequest().build();
        }

        var publicationResource = PublicationResourceFromEntityAssembler.toResourceFromEntity(publicationOptional.get());
        logger.debug("Publicación creada con éxito: {}", publicationResource);
        return new ResponseEntity<>(publicationResource, HttpStatus.CREATED);
    }

    @PutMapping("/update/{publicationId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ENTREPRENEUR', 'ROLE_ADMIN')")
    public ResponseEntity<PublicationResource> updatePublication(
            @PathVariable Long publicationId,
            @RequestBody UpdatePublicationResource resource) {

        // Verificar si la publicación existe
        var getPublicationByIdQuery = new GetPublicationByIdQuery(publicationId);
        var publicationOptional = publicationQueryService.handle(getPublicationByIdQuery);

        if (publicationOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Verificar si el usuario es admin
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        // Si no es admin, verificar que sea el dueño de la publicación
        if (!isAdmin) {
            Long currentUserId = getCurrentUserId();
            Long publicationOwnerId = publicationOptional.get().getEntrepreneurId();

            if (!currentUserId.equals(publicationOwnerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            logger.debug("Usuario admin actualizando publicación ID: {} del emprendedor ID: {}", 
                    publicationId, publicationOptional.get().getEntrepreneurId());
        }

        var updatePublicationCommand = UpdatePublicationCommandFromResourceAssembler
                .toCommandFromResource(publicationId, resource);
        var publication = publicationCommandService.handle(updatePublicationCommand);

        if (publication.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var publicationResource = PublicationResourceFromEntityAssembler
                .toResourceFromEntity(publication.get());

        return ResponseEntity.ok(publicationResource);
    }

    @DeleteMapping("/delete/{publicationId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ENTREPRENEUR', 'ROLE_ADMIN')")
    public ResponseEntity<?> deletePublication(@PathVariable Long publicationId) {
        // Verificar si la publicación existe
        var getPublicationByIdQuery = new GetPublicationByIdQuery(publicationId);
        var publicationOptional = publicationQueryService.handle(getPublicationByIdQuery);

        if (publicationOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Si el usuario es emprendedor, verificar que sea el dueño de la publicación
        // Los administradores pueden eliminar cualquier publicación
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            Long currentUserId = getCurrentUserId();
            Long publicationOwnerId = publicationOptional.get().getEntrepreneurId();

            if (!currentUserId.equals(publicationOwnerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            logger.debug("Usuario admin eliminando publicación ID: {} del emprendedor ID: {}", 
                    publicationId, publicationOptional.get().getEntrepreneurId());
        }

        boolean success = publicationCommandService.handle(publicationId);
        if (success) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof JwtUserDetails) {
            return ((JwtUserDetails) authentication.getPrincipal()).getId();
        }
        return null;
    }
} 