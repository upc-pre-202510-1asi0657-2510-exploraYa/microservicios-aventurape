package com.aventurape.stats_service.interfaces.rest.clients;

import com.aventurape.stats_service.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Cliente Feign para comunicarse con el servicio de comentarios
 * Se usa URL específica para evitar problemas de descubrimiento con Eureka
 */
@FeignClient(
    name = "comments-service", 
    url = "${feign.comments-service.url}", 
    configuration = FeignClientConfig.class
)
public interface CommentsServiceClient {
    
    /**
     * Obtiene todos los comentarios del sistema
     * @return Lista de comentarios
     */
    @GetMapping("/api/v1/comments/all-comments")
    ResponseEntity<List<CommentDto>> getAllComments();
    
    /**
     * Obtiene los comentarios de una publicación específica
     * @param publicationId ID de la publicación
     * @return Lista de comentarios de la publicación
     */
    @GetMapping("/api/v1/comments/publication/{publicationId}")
    ResponseEntity<List<CommentDto>> getCommentsByPublicationId(@PathVariable Long publicationId);
    
    /**
     * Obtiene los comentarios realizados por un usuario específico
     * @param userId ID del usuario
     * @return Lista de comentarios del usuario
     */
    @GetMapping("/api/v1/comments/user/{userId}")
    ResponseEntity<List<CommentDto>> getCommentsByUserId(@PathVariable Long userId);
    
    /**
     * Obtiene los ratings de las publicaciones de un usuario ordenados
     * @param userId ID del usuario
     * @param sortBy Campo por el que ordenar
     * @param sortDir Dirección de ordenamiento (asc o desc)
     * @return Lista de ratings de publicaciones ordenados
     */
    @GetMapping("/api/v1/comments/ratings/user/{userId}/sorted")
    ResponseEntity<List<PublicationRatingDto>> getPublicationsRatingsByUserIdSorted(
            @PathVariable("userId") Long userId,
            @RequestParam(value = "sortBy", defaultValue = "rating") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir);
    
    /**
     * Obtiene el rating de una publicación específica
     * @param publicationId ID de la publicación
     * @return DTO con el rating de la publicación
     */
    @GetMapping("/api/v1/comments/ratings/publication/{publicationId}")
    ResponseEntity<PublicationRatingDto> getPublicationRating(@PathVariable Long publicationId);
} 