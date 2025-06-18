package com.aventurape.stats_service.infrastructure.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Servicio para cargar los detalles del usuario a partir del token JWT
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(JwtUserDetailsService.class);
    private final JwtTokenUtil jwtTokenUtil;

    public JwtUserDetailsService(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * Carga los detalles del usuario a partir del nombre de usuario
     * @param username Nombre de usuario
     * @return Detalles del usuario
     * @throws UsernameNotFoundException Si no se encuentra el usuario
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.error("Este método no debería ser invocado directamente, use loadUserByToken");
        throw new UsernameNotFoundException("No se puede cargar usuario por nombre de usuario en autenticación JWT");
    }

    /**
     * Carga los detalles del usuario a partir del token JWT
     * @param token Token JWT
     * @return Detalles del usuario
     */
    public JwtUserDetails loadUserByToken(String token) {
        try {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            Long userId = jwtTokenUtil.getUserIdFromToken(token);
            List<String> roles = jwtTokenUtil.getRolesFromToken(token);
            
            if (userId == null) {
                logger.warn("No se pudo extraer el ID de usuario del token JWT. Usando nombre de usuario como identificador.");
                // Intenta extraer el ID del nombre de usuario si está en formato "id:username"
                userId = extractUserIdFromUsername(username);
            }
            
            if (roles == null) {
                logger.warn("No se encontraron roles en el token JWT. Asignando lista vacía.");
                roles = Collections.emptyList();
            }
            
            logger.debug("Cargando detalles del usuario desde token JWT: {}, ID: {}, Roles: {}", 
                    username, userId, roles);
            
            return new JwtUserDetails(username, userId, roles);
        } catch (Exception e) {
            logger.error("Error al cargar detalles del usuario desde token JWT: {}", e.getMessage());
            throw new RuntimeException("Error al cargar detalles del usuario desde token JWT", e);
        }
    }
    
    /**
     * Extrae el ID de usuario del nombre de usuario si está en formato "id:username"
     * @param username Nombre de usuario
     * @return ID de usuario o null si no se puede extraer
     */
    private Long extractUserIdFromUsername(String username) {
        try {
            if (username != null && username.contains(":")) {
                String[] parts = username.split(":");
                return Long.parseLong(parts[0]);
            }
        } catch (NumberFormatException e) {
            logger.warn("No se pudo extraer el ID de usuario del nombre de usuario: {}", username);
        }
        return null;
    }
} 