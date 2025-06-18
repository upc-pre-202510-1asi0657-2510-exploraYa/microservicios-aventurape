package com.aventurape.stats_service.interfaces.rest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.aventurape.stats_service.domain.services.StatsService;
import com.aventurape.stats_service.infrastructure.clients.PostServiceClient;
import com.aventurape.stats_service.interfaces.rest.resources.GeneralStatsResource;
import com.aventurape.stats_service.interfaces.rest.resources.PublicationByOrderResource;
import com.aventurape.stats_service.interfaces.rest.resources.PublicationStatsResource;
import com.aventurape.stats_service.interfaces.rest.transform.StatsResourceFromEntityAssembler;
import com.aventurape.stats_service.infrastructure.security.jwt.JwtUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para el servicio de estadísticas
 */
@RestController
@RequestMapping(value = "/api/v1/stats")
@Tag(name = "Stats", description = "Statistics Management Endpoints")
public class StatsController {

    private static final Logger logger = LoggerFactory.getLogger(StatsController.class);
    private final StatsService statsService;
    private final PostServiceClient postServiceClient;

    public StatsController(StatsService statsService, PostServiceClient postServiceClient) {
        this.statsService = statsService;
        this.postServiceClient = postServiceClient;
    }

    /**
     * Obtiene estadísticas para una publicación específica
     * @param publicationId ID de la publicación
     * @return Estadísticas de la publicación
     */
    @GetMapping("/publications/{publicationId}")
    @Operation(summary = "Get statistics for a specific publication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics found"),
        @ApiResponse(responseCode = "404", description = "Publication not found")
    })
    public ResponseEntity<PublicationStatsResource> getPublicationStats(@PathVariable Long publicationId) {
        logger.info("Recibida solicitud para obtener estadísticas de la publicación con ID: {}", publicationId);
        
        var stats = statsService.getPublicationStats(publicationId);
        var resource = StatsResourceFromEntityAssembler.toResourceFromEntity(stats);
        
        return ResponseEntity.ok(resource);
    }

    /**
     * Obtiene estadísticas generales del sistema
     * @return Estadísticas generales
     */
    @GetMapping("/general")
    @Operation(summary = "Get general system statistics")
    public ResponseEntity<GeneralStatsResource> getGeneralStats() {
        logger.info("Recibida solicitud para obtener estadísticas generales");
        
        var stats = statsService.getGeneralStats();
        var resource = StatsResourceFromEntityAssembler.toResourceFromEntity(stats);
        
        return ResponseEntity.ok(resource);
    }

    /**
     * Obtiene las N publicaciones mejor calificadas
     * @param limit Número máximo de publicaciones a retornar
     * @return Lista de publicaciones con sus estadísticas
     */
    @GetMapping("/publications/top-rated")
    @Operation(summary = "Get top rated publications")
    public ResponseEntity<List<PublicationStatsResource>> getTopRatedPublications(
            @RequestParam(defaultValue = "10") Integer limit) {
        logger.info("Recibida solicitud para obtener top {} publicaciones mejor calificadas", limit);
        
        var stats = statsService.getTopRatedPublications(limit);
        var resources = StatsResourceFromEntityAssembler.toResourceFromEntityList(stats);
        
        return ResponseEntity.ok(resources);
    }
    
    /**
     * Obtiene las estadísticas de las publicaciones del usuario logueado ordenadas por rating de mayor a menor
     * @return Lista de publicaciones con sus estadísticas ordenadas por rating
     */
    @GetMapping("/my-publications/by-rating")
    @Operation(summary = "Get current user's publications ordered by rating")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PublicationStatsResource>> getUserPublicationsStatsByRating() {
        try {
            // Obtener el ID del usuario del token JWT
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null) {
                logger.error("No hay autenticación disponible");
                return ResponseEntity.status(401).build(); // Unauthorized
            }
            
            Long userId = extractUserIdFromAuthentication(authentication);
            
            if (userId == null) {
                logger.error("No se pudo obtener el ID del usuario del token JWT");
                return ResponseEntity.status(401).build(); // Unauthorized
            }
            
            logger.info("Usuario autenticado con ID: {}", userId);
            logger.info("Recibida solicitud para obtener estadísticas de publicaciones del usuario {} ordenadas por rating", userId);
            
            var stats = statsService.getUserPublicationsStatsByRating(userId);
            var resources = StatsResourceFromEntityAssembler.toResourceFromEntityList(stats);
            
            return ResponseEntity.ok(resources);
        } catch (Exception e) {
            logger.error("Error al procesar la solicitud de estadísticas de publicaciones: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * Obtiene las publicaciones de un emprendedor ordenadas por rating
     * @param entrepreneurId ID del emprendedor
     * @return Lista de publicaciones ordenadas por rating
     */
    @GetMapping("/order-by-rating/{entrepreneurId}")
    @Operation(summary = "Get publications by entrepreneur ID ordered by rating")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Publications found"),
        @ApiResponse(responseCode = "400", description = "Invalid entrepreneur ID"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PublicationByOrderResource>> getPublicationsByEntrepreneurIdOrderedByRating(
            @PathVariable Long entrepreneurId) {
        logger.info("Recibida solicitud para obtener publicaciones del emprendedor {} ordenadas por rating", entrepreneurId);
        
        if (entrepreneurId == null) {
            logger.warn("ID de emprendedor nulo en la solicitud");
            return ResponseEntity.badRequest().build();
        }
        
        try {
            // Obtener el token JWT del contexto de seguridad
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String authHeader = "Bearer " + authentication.getCredentials();
            
            // Llamar al servicio para obtener las publicaciones ordenadas por rating
            List<PublicationByOrderResource> publications = 
                statsService.getFavoritePublicationsByProfileIdOrderedByRating(entrepreneurId, authHeader);
            
            return ResponseEntity.ok(publications);
        } catch (Exception e) {
            logger.error("Error al obtener publicaciones ordenadas por rating: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Extrae el ID del usuario de la autenticación
     * @param authentication Objeto de autenticación
     * @return ID del usuario o null si no se puede extraer
     */
    private Long extractUserIdFromAuthentication(Authentication authentication) {
        // Intentar obtener el ID del usuario desde JwtUserDetails
        if (authentication.getPrincipal() instanceof JwtUserDetails) {
            JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
            return userDetails.getUserId();
        }
        
        // Intentar obtener el ID del usuario desde el token JWT como credenciales
        if (authentication.getCredentials() != null) {
            try {
                // El token JWT podría estar almacenado como credenciales
                String token = authentication.getCredentials().toString();
                // Aquí podrías usar JwtTokenUtil para extraer el userId del token
                // Pero como es un fallback, simplemente registramos que estamos usando este método
                logger.debug("Intentando extraer userId desde las credenciales de autenticación");
            } catch (Exception e) {
                logger.warn("No se pudo extraer el ID del usuario desde las credenciales: {}", e.getMessage());
            }
        }
        
        // Si todo lo demás falla, intentar extraer el ID del nombre de usuario
        String username = authentication.getName();
        try {
            if (username != null && username.contains(":")) {
                String[] parts = username.split(":");
                return Long.parseLong(parts[0]);
            }
        } catch (NumberFormatException e) {
            logger.warn("No se pudo extraer el ID del usuario del nombre de usuario: {}", username);
        }
        
        return null;
    }
} 