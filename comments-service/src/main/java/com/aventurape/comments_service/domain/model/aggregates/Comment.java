package com.aventurape.comments_service.domain.model.aggregates;

import com.aventurape.comments_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment extends AuditableAbstractAggregateRoot<Comment> {

    @NotNull
    @Column(name = "publication_id")
    private Long publicationId;

    @NotNull
    @Column(name = "user_id")
    private Long userId;

    @NotBlank
    @Column(length = 1000)
    private String content;

    public Comment() {}

    public Comment(Long publicationId, Long userId, String content) {
        this.publicationId = publicationId;
        this.userId = userId;
        this.content = content;
    }

    public Comment updateContent(String content) {
        this.content = content;
        return this;
    }
} 