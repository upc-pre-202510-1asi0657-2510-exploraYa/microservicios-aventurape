package com.aventurape.favorites_service.infrastructure.security;

import com.aventurape.favorites_service.infrastructure.security.jwt.JwtRequestFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
    private final JwtRequestFilter jwtRequestFilter;

    public WebSecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.debug("Configurando SecurityFilterChain");

        http.cors(corsConfigurer -> corsConfigurer.configurationSource(request -> {
            var cors = new CorsConfiguration();
            cors.setAllowedOrigins(List.of("*"));
            cors.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE"));
            cors.setAllowedHeaders(List.of("*"));
            return cors;
        }));

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    logger.debug("Configurando reglas de autorización HTTP");
                    auth
                            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                            // Endopoints para favorites tenga el userid en el token JWT
                            .requestMatchers("/api/v1/favorites/**").permitAll()
                            //Todos los endpoints de favorites
                            .requestMatchers("/api/v1/favorites/create-favorite-publication").authenticated()
                            .requestMatchers("/api/v1/favorites/getFavoritePublicationByProfileId/{profileId}").permitAll()
                            .requestMatchers("/api/v1/favorites/getAllFavoritesPublications").permitAll()
                            .requestMatchers("/api/v1/favorites/delete-favorite-publication/{id}").authenticated()
                            .anyRequest().authenticated();
                    logger.debug("Configuración de reglas de autorización HTTP completada");
                });

        // Agregar el filtro JWT antes que el filtro de autenticación de usuario y contraseña
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        logger.debug("Filtro JWT agregado a la cadena de filtros");

        return http.build();
    }
}