package com.aventurape.stats_service.infrastructure.security;

import com.aventurape.stats_service.infrastructure.security.jwt.JwtAuthenticationConverter;
import com.aventurape.stats_service.infrastructure.security.jwt.JwtAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración de seguridad para la aplicación
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    // Rutas públicas que no requieren autenticación
    private static final String[] PUBLIC_URLS = {
        // Swagger / OpenAPI
        "/swagger-ui.html",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        // Actuator
        "/actuator/**",
        // API pública
        "/api/v1/stats/publications/**",
        "/api/v1/stats/general"
    };

    public WebSecurityConfig(JwtAuthorizationFilter jwtAuthorizationFilter, JwtAuthenticationConverter jwtAuthenticationConverter) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    /**
     * Configura la cadena de filtros de seguridad
     * @param http Configuración de seguridad HTTP
     * @return Cadena de filtros de seguridad
     * @throws Exception Si hay un error en la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.debug("Configurando SecurityFilterChain");

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    logger.debug("Configurando reglas de autorización HTTP");
                    auth
                        // Rutas públicas
                        .requestMatchers(PUBLIC_URLS).permitAll()
                        // Rutas protegidas
                        .requestMatchers("/api/v1/stats/my-publications/**").authenticated()
                        .anyRequest().authenticated();
                    logger.debug("Configuración de reglas de autorización HTTP completada");
                })
                .sessionManagement(session -> 
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html");
    }
} 