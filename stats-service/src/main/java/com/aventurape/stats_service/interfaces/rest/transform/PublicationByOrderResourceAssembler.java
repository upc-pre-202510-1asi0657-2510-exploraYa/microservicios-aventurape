package com.aventurape.stats_service.interfaces.rest.transform;

import com.aventurape.stats_service.interfaces.rest.resources.PublicationByOrderResource;

import java.util.Map;

/**
 * Transformador para convertir mapas de publicaciones a recursos PublicationByOrderResource
 */
public class PublicationByOrderResourceAssembler {
    
    /**
     * Convierte un mapa de datos de publicación a PublicationByOrderResource
     */
    public static PublicationByOrderResource toResourceFromMap(Map<String, Object> map, Double averageRating) {
        // Convertir tipos primitivos si es necesario
        Long id = toLong(map.get("id"));
        Long entrepreneurId = toLong(map.get("entrepreneurId"));
        Integer timeDuration = toInteger(map.get("timeDuration"));
        Integer cantPeople = toInteger(map.get("cantPeople"));
        Integer cost = toInteger(map.get("cost"));
        
        return new PublicationByOrderResource(
                id,
                entrepreneurId,
                (String) map.get("nameActivity"),
                (String) map.get("description"),
                timeDuration,
                (String) map.get("image"),
                cantPeople,
                cost,
                averageRating
        );
    }
    
    private static Long toLong(Object value) {
        if (value == null) return null;
        if (value instanceof Long) return (Long) value;
        if (value instanceof Integer) return ((Integer) value).longValue();
        if (value instanceof String) return Long.parseLong((String) value);
        return null;
    }
    
    private static Integer toInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Long) return ((Long) value).intValue();
        if (value instanceof String) return Integer.parseInt((String) value);
        return null;
    }
} 