package com.aventurape.favorites_service.interfaces.acl.transform;

public class PublicationDto {
    private Long publicationId;
    private Long entrepreneurId;
    private String image;
    private Integer cost;

    public PublicationDto() {
    }
    public PublicationDto(Long publicationId, Long entrepreneurId, String image, Integer cost) {
        this.publicationId = publicationId;
        this.entrepreneurId = entrepreneurId;
        this.image = image;
        this.cost = cost;
    }
    public Long getPublicationId() {
        return publicationId;
    }
    public void setPublicationId(Long publicationId) {
        this.publicationId = publicationId;
    }
    public Long getEntrepreneurId() {
        return entrepreneurId;
    }
    public void setEntrepreneurId(Long entrepreneurId) {
        this.entrepreneurId = entrepreneurId;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public Integer getCost() {
        return cost;
    }
    public void setCost(Integer cost) {
        this.cost = cost;
    }

}
