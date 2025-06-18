package com.aventurape.stats_service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración para Swagger/OpenAPI
 */
@Configuration
public class SwaggerConfig {

    @Value("${server.port:8086}")
    private String serverPort;

    /**
     * Configura OpenAPI con soporte para autenticación JWT
     * @return Configuración de OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingrese el token JWT con el prefijo Bearer: Bearer {token}")
                        )
                )
                .servers(List.of(
                        new Server().url("http://localhost:" + serverPort).description("Local server")
                ))
                .info(new Info()
                        .title("API de Estadísticas - AventuraPe")
                        .description("API REST para consultar estadísticas de publicaciones y comentarios en la plataforma AventuraPe")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Equipo AventuraPe")
                                .email("info@aventurape.com")
                                .url("https://aventurape.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                );
    }
} 