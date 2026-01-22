package com.aivle.project.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 댓글 생성 요청 DTO.
 */
@Getter
@Setter
public class CommentCreateRequest {

	@NotNull
	private Long postId;

	private Long parentId;

	@NotBlank
	private String content;
}
