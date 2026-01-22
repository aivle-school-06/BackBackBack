package com.aivle.project.post.controller;

import com.aivle.project.post.dto.PostCreateRequest;
import com.aivle.project.post.dto.PostResponse;
import com.aivle.project.post.dto.PostUpdateRequest;
import com.aivle.project.post.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자 게시글 CRUD API.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/User/posts")
public class PostController {

	private static final String USER_ID_HEADER = "X-User-Id";

	private final PostService postService;

	@GetMapping
	public ResponseEntity<List<PostResponse>> list() {
		return ResponseEntity.ok(postService.list());
	}

	@GetMapping("/{postId}")
	public ResponseEntity<PostResponse> get(@PathVariable Long postId) {
		return ResponseEntity.ok(postService.get(postId));
	}

	@PostMapping
	public ResponseEntity<PostResponse> create(
		@RequestHeader(USER_ID_HEADER) Long userId,
		@Valid @RequestBody PostCreateRequest request
	) {
		return ResponseEntity.ok(postService.create(userId, request));
	}

	@PatchMapping("/{postId}")
	public ResponseEntity<PostResponse> update(
		@RequestHeader(USER_ID_HEADER) Long userId,
		@PathVariable Long postId,
		@Valid @RequestBody PostUpdateRequest request
	) {
		return ResponseEntity.ok(postService.update(userId, postId, request));
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<Void> delete(
		@RequestHeader(USER_ID_HEADER) Long userId,
		@PathVariable Long postId
	) {
		postService.delete(userId, postId);
		return ResponseEntity.noContent().build();
	}
}
