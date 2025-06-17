package com.aventurape.favorites_service.domain.model.aggregates;


import com.aventurape.favorites_service.domain.model.valueobjects.ProfileId;
import com.aventurape.favorites_service.domain.model.valueobjects.PublicationId;
import com.aventurape.favorites_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Favorite extends AuditableAbstractAggregateRoot<Favorite> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_id")
    @Embedded
    private ProfileId profileId;

//    @ManyToOne
//    @JoinColumn(name = "publication_id", nullable = false)
    @Column(name = "publication_id")
    @Embedded
    private PublicationId publicationId;

    public Favorite() {}

    public Favorite(ProfileId profileId, PublicationId publicationId) {
        this.profileId = profileId;
        this.publicationId = publicationId;
    }
    public Favorite(Long id, ProfileId profileId, PublicationId publicationId) {
        this.id = id;
        this.profileId = profileId;
        this.publicationId = publicationId;
    }

    // Método para obtener el ID de la entidad
    public Long getId() {
        return id;
    }
    // Método para obtener el ID del perfil
    public ProfileId getProfileId() {
        return profileId;
    }
    // Método para obtener el ID de la publicación
    public PublicationId getPublicationId() {
        return publicationId;
    }

}

