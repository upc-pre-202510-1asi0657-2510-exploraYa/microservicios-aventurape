package com.aventurape.subscriptions_service.domain.model.entities;

import com.aventurape.subscriptions_service.domain.model.aggregates.Category;
import com.aventurape.subscriptions_service.domain.model.valueobjects.PublicationId;
import com.aventurape.subscriptions_service.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * PublicationCategory entity
 * This class represents a relationship between a publication and a category.
 */
@Getter
@Setter
@Entity
public class PublicationCategory extends AuditableModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Embedded
    private PublicationId publicationId;
    
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    public PublicationCategory() {
    }
    
    public PublicationCategory(PublicationId publicationId, Category category) {
        this.publicationId = publicationId;
        this.category = category;
    }
} 