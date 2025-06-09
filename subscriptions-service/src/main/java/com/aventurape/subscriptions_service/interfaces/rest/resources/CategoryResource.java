package com.aventurape.subscriptions_service.interfaces.rest.resources;

/**
 * Resource for Category
 * Used for REST API responses
 */
public class CategoryResource {
    private Long id;
    private String name;
    private Integer subscribersCount;
    
    public CategoryResource() {
    }
    
    public CategoryResource(Long id, String name, Integer subscribersCount) {
        this.id = id;
        this.name = name;
        this.subscribersCount = subscribersCount;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getSubscribersCount() {
        return subscribersCount;
    }
    
    public void setSubscribersCount(Integer subscribersCount) {
        this.subscribersCount = subscribersCount;
    }
} 