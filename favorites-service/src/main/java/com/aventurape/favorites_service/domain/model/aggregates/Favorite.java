package com.aventurape.favorites_service.domain.model.aggregates;

import com.upc.aventurape.platform.favorite.domain.model.valueobjects.ProfileId;
import com.upc.aventurape.platform.favorite.domain.model.valueobjects.PublicationId;
import com.upc.aventurape.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
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

}

