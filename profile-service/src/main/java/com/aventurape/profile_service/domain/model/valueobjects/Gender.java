package com.aventurape.profile_service.domain.model.valueobjects;

public record Gender(String gender) {
    public Gender{
        if(gender == null || gender.isEmpty()){
            throw new IllegalArgumentException("Gender is null or empty");
        }
        String normalizedGender = gender.trim().toUpperCase();
        if(!normalizedGender.equals(Genders.FEMALE.toString()) && 
           !normalizedGender.equals(Genders.MALE.toString()) && 
           !normalizedGender.equals(Genders.NONBINARY.toString())){
            throw new IllegalArgumentException("Gender is not valid. Must be one of: MALE, FEMALE, NONBINARY");
        }
    }

    public Genders convertToEnum(){
        return Genders.valueOf(gender.trim().toUpperCase());
    }
}
