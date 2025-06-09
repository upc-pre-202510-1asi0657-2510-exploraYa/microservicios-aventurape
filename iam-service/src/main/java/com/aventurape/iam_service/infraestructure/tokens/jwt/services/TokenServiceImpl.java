package com.aventurape.iam_service.infraestructure.tokens.jwt.services;

import com.aventurape.iam_service.domain.model.entities.Role;
import com.aventurape.iam_service.infraestructure.persistence.jpa.repositories.UserRepository;
import com.aventurape.iam_service.infraestructure.tokens.jwt.BearerTokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl implements BearerTokenService {
    private final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);

    private static final String AUTHORIZATION_PARAMETER_NAME = "Authorization";
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    private static final int TOKEN_BEGIN_INDEX = 7;

    private final UserRepository userRepository;

    @Value("${authorization.jwt.secret}")
    private String secret;

    @Value("${authorization.jwt.expiration.days}")
    private int expirationDays;
    
    @PostConstruct
    public void init() {
        LOGGER.info("IAM-SERVICE JWT Secret inicializado: [primeros 10 caracteres] {}", 
                  secret != null && secret.length() > 10 ? secret.substring(0, 10) + "..." : "NO DISPONIBLE");
    }

    public TokenServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String generateToken(Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        
        return buildTokenWithDefaultParameters(authentication.getName(), roles);
    }
    
    public String generateToken(String username) {
        // Obtener los roles del usuario desde la base de datos
        return userRepository.findByUsername(username)
                .map(user -> {
                    List<String> roles = user.getRoles().stream()
                            .map(Role::getAuthority)
                            .collect(Collectors.toList());
                    LOGGER.info("Generando token para el usuario {} con roles: {}", username, roles);
                    return buildTokenWithDefaultParameters(username, roles);
                })
                .orElseGet(() -> {
                    LOGGER.warn("Usuario no encontrado: {}, generando token sin roles", username);
                    return buildTokenWithDefaultParameters(username, List.of());
                });
    }
    
    private String buildTokenWithDefaultParameters(String username, List<String> roles) {
        var issuedAt = new Date();
        var expiration = DateUtils.addDays(issuedAt, expirationDays);
        var key = getSigningKey();
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        claims.put("userId", extractUserIdFromUsername(username));
        
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }
    
    // Método auxiliar para extraer el ID de usuario si está presente en el nombre de usuario
    private Long extractUserIdFromUsername(String username) {
        try {
            if (username.contains(":")) {
                String[] parts = username.split(":");
                return Long.parseLong(parts[0]);
            }
            // Si el nombre de usuario no tiene formato especial, intentamos obtener el ID de la base de datos
            return userRepository.findByUsername(username)
                    .map(user -> user.getId())
                    .orElse(null);
        } catch (Exception e) {
            LOGGER.warn("Error extracting user ID from username: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            LOGGER.info("Token is valid");
            return true;
        }  catch (SignatureException e) {
            LOGGER.error("Invalid JSON Web Token Signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JSON Web Token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("JSON Web Token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("JSON Web Token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("JSON Web Token claims string is empty: {}", e.getMessage());
        }
        return false;
    }
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenPresentIn(String authorizationParameter) {
        return StringUtils.hasText(authorizationParameter);
    }

    private boolean isBearerTokenIn(String authorizationParameter) {
        return authorizationParameter.startsWith(BEARER_TOKEN_PREFIX);
    }

    private String extractTokenFrom(String authorizationHeaderParameter) {
        return authorizationHeaderParameter.substring(TOKEN_BEGIN_INDEX);
    }

    private String getAuthorizationParameterFrom(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_PARAMETER_NAME);
    }

    @Override
    public String getBearerTokenFrom(HttpServletRequest request) {
        String parameter = getAuthorizationParameterFrom(request);
        if (isTokenPresentIn(parameter) && isBearerTokenIn(parameter))
            return extractTokenFrom(parameter);
        return null;
    }
}