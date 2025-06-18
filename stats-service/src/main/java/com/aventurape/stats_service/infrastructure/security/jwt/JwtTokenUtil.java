package com.aventurape.stats_service.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * Utilidad para manejar los tokens JWT
 * Implementa las mejores prácticas de seguridad según RFC 8725
 */
@Component
public class JwtTokenUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final int ALLOWED_CLOCK_SKEW_SECONDS = 30;

    @Value("${authorization.jwt.secret}")
    private String secret;
    
    private SecretKey signingKey;
    
    @PostConstruct
    public void init() {
        // Verificar que la clave secreta tenga suficiente longitud para HS512
        if (secret == null || secret.length() < 32) {
            logger.error("JWT Secret demasiado corto. Debe tener al menos 32 caracteres para HS256");
            throw new IllegalArgumentException("JWT Secret demasiado corto");
        } else {
            logger.info("JWT Secret inicializado correctamente. Longitud: {}", secret.length());
            signingKey = generateSigningKey();
        }
    }

    /**
     * Extrae el nombre de usuario del token
     * @param token Token JWT
     * @return Nombre de usuario
     */
    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae la fecha de expiración del token
     * @param token Token JWT
     * @return Fecha de expiración
     */
    public Date getExpirationDateFromToken(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae el ID de usuario del token
     * @param token Token JWT
     * @return ID de usuario
     */
    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Object userId = claims.get("userId");
            return userId != null ? Long.valueOf(userId.toString()) : null;
        } catch (Exception e) {
            logger.warn("Error al extraer userId del token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Extrae los roles del usuario del token
     * @param token Token JWT
     * @return Lista de roles
     */
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.get("roles", List.class);
        } catch (Exception e) {
            logger.warn("Error al extraer roles del token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Extrae un claim específico del token
     * @param token Token JWT
     * @param claimsResolver Función para extraer el claim
     * @return Valor del claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todos los claims del token
     * @param token Token JWT
     * @return Claims
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(signingKey)
                    .clockSkewSeconds(ALLOWED_CLOCK_SKEW_SECONDS) // Permitir un desfase de reloj de hasta 30 segundos
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            logger.error("Error al extraer claims del token: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Genera la clave de firma
     * @return Clave de firma
     */
    private SecretKey generateSigningKey() {
        try {
            byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            logger.error("Error al generar la clave de firma: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Valida un token JWT
     * @param token Token JWT
     * @return true si el token es válido
     */
    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            logger.error("Token JWT nulo o vacío");
            return false;
        }
        
        try {
            logger.debug("Validando token JWT");
            
            // Verificar firma y estructura del token
            Jwts.parser()
                .verifyWith(signingKey)
                .clockSkewSeconds(ALLOWED_CLOCK_SKEW_SECONDS)
                .build()
                .parseSignedClaims(token);
            
            // Verificar expiración
            boolean isNotExpired = !isTokenExpired(token);
            
            if (isNotExpired) {
                // Verificar claims adicionales para logging
                String username = getUsernameFromToken(token);
                Long userId = getUserIdFromToken(token);
                List<String> roles = getRolesFromToken(token);
                
                logger.debug("Token JWT válido. Usuario: {}, ID: {}, Roles: {}", username, userId, roles);
                return true;
            } else {
                logger.debug("Token JWT expirado");
                return false;
            }
        } catch (SignatureException e) {
            logger.error("Firma JWT inválida: {}. Verifique que la clave secreta sea la misma que en el servicio emisor.", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Token JWT malformado: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT no soportado: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error validando token JWT: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Verifica si un token ha expirado
     * @param token Token JWT
     * @return true si el token ha expirado
     */
    private boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            logger.error("Error verificando expiración del token: {}", e.getMessage());
            return true;
        }
    }
} 