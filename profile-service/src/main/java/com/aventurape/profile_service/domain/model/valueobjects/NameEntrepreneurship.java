package com.aventurape.profile_service.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record NameEntrepreneurship(String nameEntrepreneurship) {
    public NameEntrepreneurship{
        if(nameEntrepreneurship == null || nameEntrepreneurship.isEmpty()){
            throw new IllegalArgumentException("Name entrepreneurship cannot be null or empty");
        }
    }
    String getNameEntrepreneurship() { return nameEntrepreneurship; }
}
