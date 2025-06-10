package com.aventurape.post_service.domain.model.aggregates;

import com.aventurape.post_service.domain.model.entities.Adventure;
import com.aventurape.post_service.domain.model.valueobjects.EntrepreneurId;
import com.aventurape.post_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "publications")
public class Publication extends AuditableAbstractAggregateRoot<Publication> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "entrepreneur_id")
    private Long entrepreneurId;

    @Lob
    @Column(name = "image", length = 1000000000)
    private String image;

    @NotNull
    @Positive
    private Integer cost;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "adventure_id")
    private Adventure adventure;

    public Publication() {}

    public Publication(Long entrepreneurId, String image, Integer cost, Adventure adventure) {
        this.entrepreneurId = entrepreneurId;
        this.image = image;
        this.cost = cost;
        this.adventure = adventure;
    }

    public Publication updateInformation(String image, Integer cost, Adventure adventure) {
        this.image = image;
        this.cost = cost;
        this.adventure = adventure;
        return this;
    }

    // Métodos de conveniencia para acceder a datos de Adventure
    public String getNameActivity() {
        return adventure != null ? adventure.getNameActivity() : null;
    }

    public String getDescription() {
        return adventure != null ? adventure.getDescription() : null;
    }

    public Integer getTimeDuration() {
        return adventure != null ? adventure.getTimeDuration() : null;
    }

    public Integer getCantPeople() {
        return adventure != null ? adventure.getCantPeople() : null;
    }

    public Adventure getAdventure() {
        return adventure;
    }

    public Long getEntrepreneurId() {
        return entrepreneurId;
    }

    public Long getId() {
        return this.id;
    }

    public String getImage() {
        return image;
    }

    public Integer getCost() {
        return cost;
    }
} 