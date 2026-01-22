package com.aivle.project.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 댓글 수정 요청 DTO.
 */
@Getter
@Setter
public class CommentUpdateRequest {

	@NotBlank
	private String content;
}
