package com.aventurape.stats_service.domain.services.impl;

import com.aventurape.stats_service.domain.model.valueobjects.GeneralStats;
import com.aventurape.stats_service.domain.model.valueobjects.PublicationStats;
import com.aventurape.stats_service.domain.services.StatsService;
import com.aventurape.stats_service.interfaces.rest.clients.CommentDto;
import com.aventurape.stats_service.interfaces.rest.clients.CommentsServiceClient;
import com.aventurape.stats_service.infrastructure.clients.PostServiceClient;
import com.aventurape.stats_service.interfaces.rest.clients.PublicationRatingDto;
import com.aventurape.stats_service.interfaces.rest.resources.PublicationByOrderResource;
import com.aventurape.stats_service.interfaces.rest.transform.PublicationByOrderResourceAssembler;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de estadísticas
 */
@Service
public class StatsServiceImpl implements StatsService {

    private static final Logger logger = LoggerFactory.getLogger(StatsServiceImpl.class);
    private final CommentsServiceClient commentsServiceClient;
    private final PostServiceClient postServiceClient;
    private final ObjectMapper objectMapper;

    public StatsServiceImpl(CommentsServiceClient commentsServiceClient, PostServiceClient postServiceClient, ObjectMapper objectMapper) {
        this.commentsServiceClient = commentsServiceClient;
        this.postServiceClient = postServiceClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public PublicationStats getPublicationStats(Long publicationId) {
        logger.info("Obteniendo estadísticas para la publicación con ID: {}", publicationId);
        
        try {
            ResponseEntity<List<CommentDto>> commentsResponse = commentsServiceClient.getCommentsByPublicationId(publicationId);
            
            if (commentsResponse.getBody() == null || commentsResponse.getBody().isEmpty()) {
                logger.warn("No se encontraron comentarios para la publicación con ID: {}", publicationId);
                return new PublicationStats(publicationId, 0L, 0.0, 0, 0);
            }
            
            List<CommentDto> comments = commentsResponse.getBody();
            long commentCount = comments.size();
            
            // Calcular rating promedio
            double averageRating = comments.stream()
                    .filter(comment -> comment.getRating() != null)
                    .mapToInt(CommentDto::getRating)
                    .average()
                    .orElse(0.0);
            
            // Calcular rating mínimo
            int minRating = comments.stream()
                    .filter(comment -> comment.getRating() != null)
                    .mapToInt(CommentDto::getRating)
                    .min()
                    .orElse(0);
            
            // Calcular rating máximo
            int maxRating = comments.stream()
                    .filter(comment -> comment.getRating() != null)
                    .mapToInt(CommentDto::getRating)
                    .max()
                    .orElse(0);
            
            return new PublicationStats(publicationId, commentCount, averageRating, minRating, maxRating);
            
        } catch (FeignException.Unauthorized | FeignException.Forbidden e) {
            logger.error("Error de autorización al obtener comentarios para la publicación {}: {}", publicationId, e.getMessage());
            throw new SecurityException("No autorizado para acceder a los datos de comentarios", e);
        } catch (Exception e) {
            logger.error("Error al obtener estadísticas para la publicación con ID: {}", publicationId, e);
            return new PublicationStats(publicationId, 0L, 0.0, 0, 0);
        }
    }

    @Override
    public GeneralStats getGeneralStats() {
        logger.info("Obteniendo estadísticas generales del sistema");
        
        long totalComments = 0L;
        Long totalPublications = 0L;
        double averageRatingGlobal = 0.0;
        
        try {
            // Obtener número total de publicaciones
            logger.info("Solicitando total de publicaciones al servicio de publicaciones");
            ResponseEntity<Long> totalPublicationsResponse = postServiceClient.countAllPublications();
            
            if (totalPublicationsResponse.getBody() != null) {
                totalPublications = totalPublicationsResponse.getBody();
                logger.info("Total de publicaciones obtenidas: {}", totalPublications);
            } else {
                logger.warn("La respuesta del servicio de publicaciones no contiene datos");
            }
            
            // Calcular promedio de comentarios por publicación
            double averageCommentsPerPublication = totalPublications > 0 ? (double) totalComments / totalPublications : 0.0;
            
            return new GeneralStats(totalPublications, totalComments, averageRatingGlobal, averageCommentsPerPublication);
            
        } catch (FeignException.Unauthorized | FeignException.Forbidden e) {
            logger.error("Error de autorización al obtener estadísticas generales: {}", e.getMessage());
            throw new SecurityException("No autorizado para acceder a los datos de estadísticas", e);
        } catch (Exception e) {
            logger.error("Error al obtener estadísticas generales", e);
            return new GeneralStats(0L, 0L, 0.0, 0.0);
        }
    }

    @Override
    public List<PublicationStats> getTopRatedPublications(Integer limit) {
        logger.info("Obteniendo top {} publicaciones mejor calificadas", limit);
        
        try {
            // Implementación pendiente - se necesita un endpoint en el servicio de comentarios
            // que devuelva las publicaciones ordenadas por rating
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error al obtener top publicaciones", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<PublicationStats> getUserPublicationsStatsByRating(Long userId) {
        logger.info("Obteniendo estadísticas de publicaciones para el usuario con ID: {}", userId);
        
        try {
            // Obtener los ratings de las publicaciones del usuario ordenados por rating
            ResponseEntity<List<PublicationRatingDto>> ratingsResponse = 
                commentsServiceClient.getPublicationsRatingsByUserIdSorted(userId, "rating", "desc");
            
            if (ratingsResponse.getBody() == null || ratingsResponse.getBody().isEmpty()) {
                logger.info("No se encontraron ratings para las publicaciones del usuario {}", userId);
                return new ArrayList<>();
            }
            
            List<PublicationRatingDto> ratings = ratingsResponse.getBody();
            logger.info("Ratings obtenidos para el usuario {}: {}", userId, ratings.size());
            
            // Convertir los ratings a estadísticas de publicaciones
            return ratings.stream()
                .map(rating -> new PublicationStats(
                    rating.getPublicationId(),
                    rating.getCommentCount(),
                    rating.getAverageRating(),
                    rating.getMinRating(),
                    rating.getMaxRating()
                ))
                .collect(Collectors.toList());
            
        } catch (FeignException.Unauthorized | FeignException.Forbidden e) {
            logger.error("Error de autorización al obtener estadísticas para el usuario {}: {}", userId, e.getMessage());
            throw new SecurityException("No autorizado para acceder a los datos de estadísticas", e);
        } catch (Exception e) {
            logger.error("Error al obtener estadísticas de publicaciones para el usuario con ID: {}", userId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<PublicationByOrderResource> getFavoritePublicationsByProfileIdOrderedByRating(Long profileId, String authorizationHeader) {
        try {
            logger.info("Obteniendo publicaciones favoritas para el perfil {} ordenadas por rating", profileId);
            
            // Obtener publicaciones del emprendedor desde el servicio de publicaciones
            List<Map<String, Object>> publicationsResponse = postServiceClient.getPublicationsByEntrepreneurIdOrderedByRating(profileId, authorizationHeader);
            
            if (publicationsResponse == null || publicationsResponse.isEmpty()) {
                logger.info("No se encontraron publicaciones para el emprendedor {}", profileId);
                return new ArrayList<>();
            }
            
            logger.info("Se encontraron {} publicaciones para el emprendedor {}", publicationsResponse.size(), profileId);
            
            // Lista para almacenar las publicaciones con su rating
            List<PublicationWithRatingImpl> publicationsWithRating = new ArrayList<>();
            
            // Para cada publicación, obtener su rating
            for (Map<String, Object> publicationMap : publicationsResponse) {
                // Usar un método seguro para convertir el ID
                Long publicationId = toLong(publicationMap.get("id"));
                
                if (publicationId == null) {
                    logger.warn("Publicación sin ID encontrada para el emprendedor {}", profileId);
                    continue;
                }
                
                // Obtener el rating de la publicación
                Double averageRating = getPublicationAverageRating(publicationId);
                
                publicationsWithRating.add(new PublicationWithRatingImpl(publicationMap, averageRating));
            }
            
            // Ordenar las publicaciones por rating de mayor a menor
            publicationsWithRating.sort(Comparator.comparing(PublicationWithRatingImpl::getAverageRating).reversed());
            
            // Convertir las publicaciones ordenadas a recursos
            List<PublicationByOrderResource> result = publicationsWithRating.stream()
                .map(p -> PublicationByOrderResourceAssembler.toResourceFromMap(p.getPublicationData(), p.getAverageRating()))
                .collect(Collectors.toList());
            
            logger.info("Publicaciones ordenadas por rating: {}", result.size());
            return result;
        } catch (Exception e) {
            logger.error("Error al obtener publicaciones favoritas ordenadas por rating: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener publicaciones favoritas ordenadas por rating", e);
        }
    }
    
    /**
     * Convierte de forma segura un objeto a Long
     */
    private Long toLong(Object value) {
        if (value == null) return null;
        if (value instanceof Long) return (Long) value;
        if (value instanceof Integer) return ((Integer) value).longValue();
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    /**
     * Obtiene el rating promedio de una publicación
     * @param publicationId ID de la publicación
     * @return Rating promedio
     */
    private Double getPublicationAverageRating(Long publicationId) {
        try {
            // Intentar obtener el rating directamente del servicio de comentarios
            ResponseEntity<PublicationRatingDto> ratingResponse = commentsServiceClient.getPublicationRating(publicationId);
            
            if (ratingResponse.getBody() != null && ratingResponse.getBody().getAverageRating() != null) {
                return ratingResponse.getBody().getAverageRating();
            }
            
            // Si no se pudo obtener el rating directamente, calcularlo a partir de los comentarios
            ResponseEntity<List<CommentDto>> commentsResponse = commentsServiceClient.getCommentsByPublicationId(publicationId);
            
            if (commentsResponse.getBody() != null && !commentsResponse.getBody().isEmpty()) {
                List<CommentDto> comments = commentsResponse.getBody();
                
                return comments.stream()
                        .filter(comment -> comment.getRating() != null)
                        .mapToInt(CommentDto::getRating)
                        .average()
                        .orElse(0.0);
            }
            
            return 0.0;
        } catch (Exception e) {
            logger.error("Error al obtener rating para la publicación {}: {}", publicationId, e.getMessage());
            return 0.0;
        }
    }
    
    /**
     * Clase interna para representar una publicación con su rating
     */
    private static class PublicationWithRatingImpl {
        private final Map<String, Object> publicationData;
        private final Double averageRating;
        
        public PublicationWithRatingImpl(Map<String, Object> publicationData, Double averageRating) {
            this.publicationData = publicationData;
            this.averageRating = averageRating;
        }
        
        public Map<String, Object> getPublicationData() {
            return publicationData;
        }
        
        public Double getAverageRating() {
            return averageRating != null ? averageRating : 0.0;
        }
    }
} 