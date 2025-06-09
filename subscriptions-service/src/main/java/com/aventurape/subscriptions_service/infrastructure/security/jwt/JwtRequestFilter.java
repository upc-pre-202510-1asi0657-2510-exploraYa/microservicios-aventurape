package com.aventurape.subscriptions_service.infrastructure.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    private final JwtTokenUtil jwtTokenUtil;

    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            logger.debug("JwtRequestFilter procesando solicitud para URI: {}", request.getRequestURI());
            
            String jwt = extractJwtFromRequest(request);
            
            if (jwt == null) {
                logger.debug("No se encontró token JWT en la solicitud");
            } else {
                logger.debug("Token JWT encontrado: {}", jwt);
                
                if (jwtTokenUtil.validateToken(jwt)) {
                    logger.debug("Token JWT validado correctamente");
                    
                    String username = jwtTokenUtil.getUsernameFromToken(jwt);
                    Long userId = jwtTokenUtil.getUserIdFromToken(jwt);
                    List<String> roles = jwtTokenUtil.getRolesFromToken(jwt);
                    
                    logger.debug("Username extraído del token: {}", username);
                    logger.debug("UserId extraído del token: {}", userId);
                    logger.debug("Roles extraídos del token: {}", roles);

                    if (StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                        List<SimpleGrantedAuthority> authorities = roles.stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());
                        
                        logger.debug("Autoridades creadas: {}", authorities);

                        JwtUserDetails userDetails = new JwtUserDetails(
                                userId,
                                username,
                                authorities
                        );

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                authorities
                        );

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        
                        logger.debug("Autenticación establecida en el SecurityContextHolder");
                    } else {
                        logger.debug("No se estableció la autenticación: username vacío o ya existe autenticación");
                    }
                } else {
                    logger.debug("El token JWT no se pudo validar");
                }
            }
        } catch (Exception e) {
            logger.error("No se pudo establecer la autenticación del usuario: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        logger.debug("Header Authorization: {}", bearerToken);
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
} 