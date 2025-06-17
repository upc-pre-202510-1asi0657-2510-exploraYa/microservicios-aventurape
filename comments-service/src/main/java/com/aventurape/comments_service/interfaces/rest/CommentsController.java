package com.aventurape.comments_service.interfaces.rest;

import com.aventurape.comments_service.domain.model.queries.GetAllCommentsQuery;
import com.aventurape.comments_service.domain.model.queries.GetCommentByIdQuery;
import com.aventurape.comments_service.domain.model.queries.GetCommentsByPublicationIdQuery;
import com.aventurape.comments_service.domain.model.queries.GetCommentsByUserIdQuery;
import com.aventurape.comments_service.domain.services.CommentCommandService;
import com.aventurape.comments_service.domain.services.CommentQueryService;
import com.aventurape.comments_service.infrastructure.security.jwt.JwtUserDetails;
import com.aventurape.comments_service.interfaces.rest.clients.PostServiceClient;
import com.aventurape.comments_service.interfaces.rest.resources.CommentResource;
import com.aventurape.comments_service.interfaces.rest.resources.CreateCommentResource;
import com.aventurape.comments_service.interfaces.rest.resources.UpdateCommentResource;
import com.aventurape.comments_service.interfaces.rest.transform.CommentResourceFromEntityAssembler;
import com.aventurape.comments_service.interfaces.rest.transform.CreateCommentCommandFromResourceAssembler;
import com.aventurape.comments_service.interfaces.rest.transform.UpdateCommentCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/comments")
@Tag(name = "Comments", description = "Comment Management Endpoints")
public class CommentsController {

    private static final Logger logger = LoggerFactory.getLogger(CommentsController.class);
    private final CommentQueryService commentQueryService;
    private final CommentCommandService commentCommandService;
    private final PostServiceClient postServiceClient;

    /**
     * Constructor for CommentsController.
     *
     * @param commentQueryService  Service for handling comment queries.
     * @param commentCommandService Service for handling comment commands.
     * @param postServiceClient     Client for interacting with the Post service.
     */

    public CommentsController(CommentQueryService commentQueryService,
                           CommentCommandService commentCommandService,
                           PostServiceClient postServiceClient) {
        this.commentQueryService = commentQueryService;
        this.commentCommandService = commentCommandService;
        this.postServiceClient = postServiceClient;
    }

    @GetMapping("/all-comments")
    @Transactional
    public ResponseEntity<List<CommentResource>> getAllComments() {
        var getAllCommentsQuery = new GetAllCommentsQuery();
        var comments = commentQueryService.handle(getAllCommentsQuery);
        var commentResources = comments.stream()
                .map(CommentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(commentResources);
    }

    @GetMapping("/{commentId}")
    @Transactional
    public ResponseEntity<CommentResource> getCommentById(@PathVariable Long commentId) {
        var getCommentByIdQuery = new GetCommentByIdQuery(commentId);
        var comment = commentQueryService.handle(getCommentByIdQuery);
        if (comment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var commentResource = CommentResourceFromEntityAssembler.toResourceFromEntity(comment.get());
        return ResponseEntity.ok(commentResource);
    }

    @GetMapping("/publication/{publicationId}")
    @Transactional
    public ResponseEntity<List<CommentResource>> getCommentsByPublicationId(@PathVariable Long publicationId) {
        // Verificar si la publicación existe
        try {
            var publicationExists = postServiceClient.existsPublicationById(publicationId);
            if (publicationExists.getBody() == null || !publicationExists.getBody()) {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.warn("Error al verificar la publicación: {}", e.getMessage());
            // Continuamos aunque no podamos verificar la publicación
        }

        var getCommentsByPublicationIdQuery = new GetCommentsByPublicationIdQuery(publicationId);
        var comments = commentQueryService.handle(getCommentsByPublicationIdQuery);
        var commentResources = comments.stream()
                .map(CommentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(commentResources);
    }

    @GetMapping("/user/{userId}")
    @Transactional
    public ResponseEntity<List<CommentResource>> getCommentsByUserId(@PathVariable Long userId) {
        var getCommentsByUserIdQuery = new GetCommentsByUserIdQuery(userId);
        var comments = commentQueryService.handle(getCommentsByUserIdQuery);
        var commentResources = comments.stream()
                .map(CommentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(commentResources);
    }

    @GetMapping("/public/publication/{publicationId}")
    @Transactional
    public ResponseEntity<List<CommentResource>> getPublicCommentsByPublicationId(@PathVariable Long publicationId) {
        return getCommentsByPublicationId(publicationId);
    }

    @GetMapping("/test-auth")
    public ResponseEntity<Map<String, Object>> testAuthentication() {
        logger.debug("Endpoint de prueba de autenticación llamado");
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("Autenticación actual: {}", authentication);
        
        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", authentication != null && authentication.isAuthenticated());
        response.put("principal", authentication != null ? authentication.getPrincipal() : null);
        response.put("authorities", authentication != null ? authentication.getAuthorities() : null);
        
        if (authentication != null && authentication.getPrincipal() instanceof JwtUserDetails) {
            JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
            response.put("userId", userDetails.getId());
            response.put("username", userDetails.getUsername());
        }
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-comment")
    @Transactional
    public ResponseEntity<CommentResource> createComment(@RequestBody CreateCommentResource resource) {
        logger.debug("Recibida solicitud para crear comentario: {}", resource);
        
        // Obtener autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("Autenticación actual: {}", authentication);
        
        // Obtener el ID del usuario actual del token JWT
        Long currentUserId = getCurrentUserId();
        logger.debug("ID de usuario actual: {}", currentUserId);

        // Asegurarse de que el ID del usuario en el comentario coincida con el ID del usuario autenticado
        if (currentUserId == null || !currentUserId.equals(resource.userId())) {
            logger.debug("Acceso prohibido: el ID del usuario ({}) no coincide con el ID del usuario autenticado ({})", 
                    resource.userId(), currentUserId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Verificar si la publicación existe
        try {
            var publicationExists = postServiceClient.existsPublicationById(resource.publicationId());
            if (publicationExists.getBody() == null || !publicationExists.getBody()) {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            logger.warn("Error al verificar la publicación: {}", e.getMessage());
            // Continuamos aunque no podamos verificar la publicación
        }

        var createCommentCommand = CreateCommentCommandFromResourceAssembler.toCommandFromResource(resource);
        var commentOptional = commentCommandService.handle(createCommentCommand);

        if (commentOptional.isEmpty()) {
            logger.debug("El comentario no pudo ser creado");
            return ResponseEntity.badRequest().build();
        }

        var commentResource = CommentResourceFromEntityAssembler.toResourceFromEntity(commentOptional.get());
        logger.debug("Comentario creado con éxito: {}", commentResource);
        return new ResponseEntity<>(commentResource, HttpStatus.CREATED);
    }

    @PutMapping("/update/{commentId}")
    @Transactional
    public ResponseEntity<CommentResource> updateComment(
            @PathVariable Long commentId,
            @RequestBody UpdateCommentResource resource) {

        // Verificar si el comentario existe
        var getCommentByIdQuery = new GetCommentByIdQuery(commentId);
        var commentOptional = commentQueryService.handle(getCommentByIdQuery);

        if (commentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Verificar si el usuario es el dueño del comentario
        Long currentUserId = getCurrentUserId();
        Long commentOwnerId = commentOptional.get().getUserId();

        if (!currentUserId.equals(commentOwnerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var updateCommentCommand = UpdateCommentCommandFromResourceAssembler
                .toCommandFromResource(commentId, resource);
        var comment = commentCommandService.handle(updateCommentCommand);

        if (comment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var commentResource = CommentResourceFromEntityAssembler
                .toResourceFromEntity(comment.get());

        return ResponseEntity.ok(commentResource);
    }

    @DeleteMapping("/delete/{commentId}")
    @Transactional
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        // Verificar si el comentario existe
        var getCommentByIdQuery = new GetCommentByIdQuery(commentId);
        var commentOptional = commentQueryService.handle(getCommentByIdQuery);

        if (commentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Verificar si el usuario es el dueño del comentario
        Long currentUserId = getCurrentUserId();
        Long commentOwnerId = commentOptional.get().getUserId();

        if (!currentUserId.equals(commentOwnerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        boolean success = commentCommandService.handle(commentId);
        if (success) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof JwtUserDetails) {
            JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
            return userDetails.getId();
        }
        return null;
    }
} 