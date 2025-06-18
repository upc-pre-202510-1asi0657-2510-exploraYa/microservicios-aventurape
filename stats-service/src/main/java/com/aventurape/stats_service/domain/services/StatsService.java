package com.aventurape.stats_service.domain.services;

import com.aventurape.stats_service.domain.model.valueobjects.GeneralStats;
import com.aventurape.stats_service.domain.model.valueobjects.PublicationStats;
import com.aventurape.stats_service.interfaces.rest.resources.PublicationByOrderResource;

import java.util.List;

/**
 * Interfaz para el servicio de estadísticas
 */
public interface StatsService {
    
    /**
     * Obtiene estadísticas para una publicación específica
     * @param publicationId ID de la publicación
     * @return Objeto con estadísticas de la publicación
     */
    PublicationStats getPublicationStats(Long publicationId);
    
    /**
     * Obtiene estadísticas generales del sistema
     * @return Objeto con estadísticas generales
     */
    GeneralStats getGeneralStats();
    
    /**
     * Obtiene estadísticas para las N publicaciones con mejor calificación promedio
     * @param limit Cantidad de publicaciones a retornar
     * @return Lista de estadísticas de publicaciones ordenadas por calificación
     */
    List<PublicationStats> getTopRatedPublications(Integer limit);
    
    /**
     * Obtiene estadísticas para las publicaciones de un usuario específico
     * ordenadas por calificación de mayor a menor
     * @param userId ID del usuario
     * @return Lista de estadísticas de publicaciones del usuario ordenadas por calificación
     */
    List<PublicationStats> getUserPublicationsStatsByRating(Long userId);

    /**
     * Obtiene publicaciones de un emprendedor ordenadas por rating
     * @param profileId ID del perfil del emprendedor
     * @param authorizationHeader Token de autorización
     * @return Lista de publicaciones ordenadas por rating
     */
    List<PublicationByOrderResource> getFavoritePublicationsByProfileIdOrderedByRating(Long profileId, String authorizationHeader);
} 