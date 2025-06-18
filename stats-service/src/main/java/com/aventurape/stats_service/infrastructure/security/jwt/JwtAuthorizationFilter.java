package com.aventurape.stats_service.infrastructure.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro para autorización basada en JWT
 * Se ejecuta una vez por cada solicitud para validar el token JWT
 */
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;

    public JwtAuthorizationFilter(JwtTokenUtil jwtTokenUtil, JwtUserDetailsService jwtUserDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        try {
            // Obtener el token del encabezado Authorization
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            
            logger.debug("Procesando solicitud para URI: {}", request.getRequestURI());
            logger.debug("Header Authorization: {}", authorizationHeader);
            
            if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
                logger.debug("No se encontró token JWT en la solicitud o no tiene formato Bearer");
                filterChain.doFilter(request, response);
                return;
            }
            
            // Extraer el token sin el prefijo "Bearer "
            String token = authorizationHeader.substring(BEARER_PREFIX.length());
            
            // Validar el token
            if (jwtTokenUtil.validateToken(token)) {
                // Obtener el nombre de usuario y el ID del token
                String username = jwtTokenUtil.getUsernameFromToken(token);
                Long userId = jwtTokenUtil.getUserIdFromToken(token);
                
                logger.debug("Token JWT válido para el usuario: {}, ID: {}", username, userId);
                
                // Cargar los detalles del usuario
                JwtUserDetails userDetails = jwtUserDetailsService.loadUserByToken(token);
                
                // Crear la autenticación
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, token, userDetails.getAuthorities());
                
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Establecer la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                logger.debug("Autenticación establecida en el contexto de seguridad");
            } else {
                logger.warn("Token JWT inválido");
            }
        } catch (Exception e) {
            logger.error("Error al procesar el token JWT: {}", e.getMessage(), e);
        }
        
        filterChain.doFilter(request, response);
    }
} 