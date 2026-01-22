package com.aivle.project.comment.dto;

import com.aivle.project.comment.entity.CommentsEntity;
import java.time.LocalDateTime;

/**
 * 댓글 응답 DTO.
 */
public record CommentResponse(
	Long id,
	Long userId,
	Long postId,
	Long parentId,
	String content,
	int depth,
	int sequence,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static CommentResponse from(CommentsEntity comment) {
		return new CommentResponse(
			comment.getId(),
			comment.getUser().getId(),
			comment.getPost().getId(),
			comment.getParent() != null ? comment.getParent().getId() : null,
			comment.getContent(),
			comment.getDepth(),
			comment.getSequence(),
			comment.getCreatedAt(),
			comment.getUpdatedAt()
		);
	}
}
