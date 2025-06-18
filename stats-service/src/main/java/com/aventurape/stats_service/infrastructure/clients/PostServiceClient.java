package com.aventurape.stats_service.infrastructure.clients;

import com.aventurape.stats_service.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;

@FeignClient(
    name = "post-service",
    url = "${feign.post-service.url}",
    configuration = FeignClientConfig.class
)
public interface PostServiceClient {
    
    /**
     * Obtiene las publicaciones de un emprendedor ordenadas por rating
     * @param entrepreneurId ID del emprendedor
     * @param authorizationHeader Token de autorización
     * @return Lista de publicaciones ordenadas por rating
     */
    @GetMapping("/api/v1/publications/order-by-rating/{entrepreneurId}")
    List<Map<String, Object>> getPublicationsByEntrepreneurIdOrderedByRating(
            @PathVariable("entrepreneurId") Long entrepreneurId,
            @RequestHeader("Authorization") String authorizationHeader);
            
    /**
     * Obtiene el número total de publicaciones
     * @return Número total de publicaciones
     */
    @GetMapping("/api/v1/publications/count")
    ResponseEntity<Long> countAllPublications();
} 