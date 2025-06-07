package com.aventurape.profile_service.domain.model.valueobjects;

public record Gender(String gender) {
    public Gender{
        if(gender == null || gender.isEmpty()){
            throw new IllegalArgumentException("Gender is null or empty");
        }
        if(!gender.equals(Genders.FEMALE.toString()) && !gender.equals(Genders.MALE.toString()) && !gender.equals(Genders.NONBINARY.toString())){
            throw new IllegalArgumentException("Gender is not valid");
        }
    }

    public Genders convertToEnum(){
        return Genders.valueOf(gender);
    }
}
