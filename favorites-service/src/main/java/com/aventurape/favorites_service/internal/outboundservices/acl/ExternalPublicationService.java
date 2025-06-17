package com.aventurape.favorites_service.internal.outboundservices.acl;

import com.aventurape.favorites_service.interfaces.acl.transform.PublicationDto;
import com.aventurape.favorites_service.interfaces.acl.transform.PublicationIdDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class ExternalPublicationService {

    private final RestTemplate restTemplate;
    private final String publicationServiceUrl;

    public ExternalPublicationService(
            RestTemplate restTemplate,
            @Value("${services.publication.url}") String publicationServiceUrl) {
        this.restTemplate = restTemplate;
        this.publicationServiceUrl = publicationServiceUrl;
    }

    public Optional<PublicationIdDto> fetchPublicationById(Long publicationId) {
        try {
            String url = publicationServiceUrl + "/api/v1/publications/" + publicationId;
            ResponseEntity<PublicationDto> response =
                    restTemplate.getForEntity(url, PublicationDto.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return Optional.of(new PublicationIdDto(response.getBody().getPublicationId()));
            }
            return Optional.empty();
        } catch (Exception e) {
            // Log error
            return Optional.empty();
        }
    }

    public boolean existsPublicationById(Long publicationId) {
        try {
            // Corregir a la ruta correcta
            String url = publicationServiceUrl + "/api/v1/publications/" + publicationId;
            ResponseEntity<PublicationDto> response = restTemplate.getForEntity(url, PublicationDto.class);
            // Si la respuesta es exitosa, la publicación existe
            return response.getStatusCode().is2xxSuccessful() && response.getBody() != null;
        } catch (Exception e) {
            // La publicación no existe o hubo un error
            return false;
        }
    }
}