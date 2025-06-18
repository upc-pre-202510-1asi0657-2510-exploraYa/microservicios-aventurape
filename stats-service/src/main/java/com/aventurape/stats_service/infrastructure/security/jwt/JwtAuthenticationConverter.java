package com.aventurape.stats_service.infrastructure.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Conversor para transformar un JWT en un token de autenticación
 */
@Component
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationConverter.class);
    private static final String ROLES_CLAIM_NAME = "roles";

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        
        String username = jwt.getSubject();
        Long userId = extractUserId(jwt);
        
        logger.debug("Convirtiendo JWT a token de autenticación para usuario: {}, ID: {}", username, userId);
        
        return new JwtAuthenticationToken(jwt, authorities);
    }

    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        try {
            List<String> roles = jwt.getClaim(ROLES_CLAIM_NAME);
            
            if (roles == null || roles.isEmpty()) {
                logger.debug("No se encontraron roles en el token JWT");
                return Collections.emptyList();
            }
            
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al extraer roles del token JWT: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private Long extractUserId(Jwt jwt) {
        try {
            Object userId = jwt.getClaim("userId");
            return userId != null ? Long.valueOf(userId.toString()) : null;
        } catch (Exception e) {
            logger.error("Error al extraer ID de usuario del token JWT: {}", e.getMessage());
            return null;
        }
    }
} 