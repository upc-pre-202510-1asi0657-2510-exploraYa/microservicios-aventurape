package com.aventurape.subscriptions_service.interfaces.rest.resources;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request para crear una suscripción a categoría para cualquier usuario
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubscriptionRequest {
    /**
     * ID del usuario que se suscribe
     */
    private Long userId;
    
    /**
     * ID de la categoría a la que se suscribe
     */
    private Long categoryId;
} 