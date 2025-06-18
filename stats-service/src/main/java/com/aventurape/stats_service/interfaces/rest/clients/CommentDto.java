package com.aventurape.stats_service.interfaces.rest.clients;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * DTO para representar un comentario del servicio de comentarios
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private Long publicationId;
    private Long userId;
    private String content;
    private Integer rating;
    private Date createdAt;
    private Date updatedAt;
} 