package com.aventurape.comments_service.infrastructure.persistence.jpa.repositories;

import com.aventurape.comments_service.domain.model.aggregates.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    Optional<Comment> findById(Long id);
    List<Comment> findByPublicationId(Long publicationId);
    List<Comment> findByUserId(Long userId);
    boolean existsById(Long id);
} 