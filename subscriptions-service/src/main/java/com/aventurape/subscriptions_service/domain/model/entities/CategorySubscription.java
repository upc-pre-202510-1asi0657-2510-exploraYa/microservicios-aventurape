package com.aventurape.subscriptions_service.domain.model.entities;

import com.aventurape.subscriptions_service.domain.model.aggregates.Category;
import com.aventurape.subscriptions_service.domain.model.valueobjects.UserId;
import com.aventurape.subscriptions_service.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * CategorySubscription entity
 * This class represents a subscription of a user to a category.
 */
@Getter
@Setter
@Entity
public class CategorySubscription extends AuditableModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private UserId userId;
    
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date subscribedAt;
    
    public CategorySubscription() {
        this.subscribedAt = new Date();
    }
    
    public CategorySubscription(UserId userId, Category category) {
        this.userId = userId;
        this.category = category;
        this.subscribedAt = new Date();
    }
} 