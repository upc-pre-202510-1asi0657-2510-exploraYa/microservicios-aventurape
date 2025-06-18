package com.aventurape.stats_service.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.aventurape.stats_service.infrastructure.security.jwt.JwtUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Configuración específica para clientes Feign
 */
@Configuration
public class FeignClientConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(FeignClientConfig.class);

    /**
     * Crea un interceptor de solicitudes para añadir el token JWT a las solicitudes Feign
     * Este interceptor propaga el token de autenticación del usuario actual a los servicios invocados
     * @return Interceptor de solicitudes
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            try {
                // Obtener el token JWT de la solicitud actual
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (requestAttributes != null) {
                    HttpServletRequest request = requestAttributes.getRequest();
                    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
                    
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        logger.debug("Propagando token JWT a solicitud Feign: {}", authHeader);
                        requestTemplate.header(HttpHeaders.AUTHORIZATION, authHeader);
                    } else {
                        // Intentar obtener el token del contexto de seguridad como respaldo
                        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                        if (authentication != null && authentication.getCredentials() != null) {
                            String token = authentication.getCredentials().toString();
                            logger.debug("Propagando token JWT desde el contexto de seguridad");
                            requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
                        } else {
                            logger.debug("No se encontró token JWT para propagar");
                        }
                    }
                } else {
                    logger.debug("No hay contexto de solicitud disponible");
                }
            } catch (Exception e) {
                logger.error("Error al propagar token JWT a solicitud Feign: {}", e.getMessage());
            }
        };
    }
} 