package com.aventurape.subscriptions_service.interfaces.acl.transform;

/**
 * DTO for Category
 * Used for communication with other bounded contexts
 */
public class CategoryDto {
    private Long id;
    private String name;
    private Integer subscribersCount;
    
    public CategoryDto() {
    }
    
    public CategoryDto(Long id, String name, Integer subscribersCount) {
        this.id = id;
        this.name = name;
        this.subscribersCount = subscribersCount;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public Integer getSubscribersCount() {
        return subscribersCount;
    }
} 