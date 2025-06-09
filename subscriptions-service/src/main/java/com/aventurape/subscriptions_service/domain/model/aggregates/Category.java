package com.aventurape.subscriptions_service.domain.model.aggregates;

import com.aventurape.subscriptions_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Category aggregate root
 * This class represents a category for adventure publications.
 */
@Getter
@Setter
@Entity
public class Category extends AuditableAbstractAggregateRoot<Category> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 50)
    @Column(unique = true)
    private String name;
    
    @NotNull
    @Column(nullable = false)
    private Integer subscribersCount;
    
    /**
     * Default constructor
     */
    public Category() {
        this.subscribersCount = 0;
    }
    
    /**
     * Parameterized constructor
     * @param name the name of the category
     */
    public Category(String name) {
        this.name = name;
        this.subscribersCount = 0;
    }
    
    /**
     * Increment the subscribers count
     */
    public void incrementSubscribersCount() {
        this.subscribersCount++;
    }
    
    /**
     * Decrement the subscribers count
     */
    public void decrementSubscribersCount() {
        if (this.subscribersCount > 0) {
            this.subscribersCount--;
        }
    }
} 