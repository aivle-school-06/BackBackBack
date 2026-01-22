package com.aivle.project.post.repository;

import com.aivle.project.post.entity.PostsEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 게시글 조회/저장 리포지토리.
 */
public interface PostsRepository extends JpaRepository<PostsEntity, Long> {

	Optional<PostsEntity> findByIdAndDeletedAtIsNull(Long id);

	List<PostsEntity> findAllByDeletedAtIsNullOrderByCreatedAtDesc();
}
