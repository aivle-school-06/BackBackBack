package com.aivle.project.comment.repository;

import com.aivle.project.comment.entity.CommentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<CommentsEntity, Long> {
    List<CommentsEntity> findByPostIdOrderByDepthAscSequenceAsc(Long postId);
}
