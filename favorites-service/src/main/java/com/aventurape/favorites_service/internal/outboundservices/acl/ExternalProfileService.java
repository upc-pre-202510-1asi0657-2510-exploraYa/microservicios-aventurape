package com.aventurape.favorites_service.internal.outboundservices.acl;

import org.springframework.beans.factory.annotation.Value;
import com.aventurape.favorites_service.interfaces.acl.transform.ProfileIdDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

// Aca se encuentra el servicio externo que permite obtener el ID del perfil de un usuario
// como podemos ver conectamos nuestro acl con el boundary de profiles
@Service
public class ExternalProfileService {
    private final RestTemplate restTemplate;
    private final String profileServiceUrl;

    public ExternalProfileService(RestTemplate restTemplate,
                                  @Value("${services.profile.url}") String profileServiceUrl) {
        this.restTemplate = restTemplate;
        this.profileServiceUrl = profileServiceUrl;
    }
    public Optional<ProfileIdDto> fetchProfileIdByUserId(Long userId) {
        try {
            String url = profileServiceUrl + "/api/profiles/user/" + userId;
            ProfileIdDto response = restTemplate.getForObject(url, ProfileIdDto.class);
            return Optional.ofNullable(response);
        } catch (Exception e) {
            // Log error
            return Optional.empty();
        }
    }
    public boolean existsProfileById(Long profileId) {
        try {
            String url = profileServiceUrl + "/api/profiles/" + profileId + "/exists";
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            return response.getStatusCode().is2xxSuccessful() &&
                    Boolean.TRUE.equals(response.getBody());
        } catch (Exception e) {
            // Loguear error
            return false;
        }
    }
    public Optional<ProfileIdDto> fetchProfileById(Long profileId) {
        try {
            String url = profileServiceUrl + "/api/profiles/" + profileId;
            ProfileIdDto response = restTemplate.getForObject(url, ProfileIdDto.class);
            return Optional.ofNullable(response);
        } catch (Exception e) {
            // Log error
            return Optional.empty();
        }
    }
    public boolean existsProfileByUserId(Long userId) {
        try {
            String url = profileServiceUrl + "/api/profiles/user/" + userId + "/exists";
            Boolean response = restTemplate.getForObject(url, Boolean.class);
            return Boolean.TRUE.equals(response);
        } catch (Exception e) {
            // Log error
            return false;
        }
    }
}