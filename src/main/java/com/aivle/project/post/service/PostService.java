package com.aivle.project.post.service;

import com.aivle.project.category.entity.CategoriesEntity;
import com.aivle.project.category.repository.CategoriesRepository;
import com.aivle.project.common.error.CommonErrorCode;
import com.aivle.project.common.error.CommonException;
import com.aivle.project.post.dto.PostCreateRequest;
import com.aivle.project.post.dto.PostResponse;
import com.aivle.project.post.dto.PostUpdateRequest;
import com.aivle.project.post.entity.PostStatus;
import com.aivle.project.post.entity.PostsEntity;
import com.aivle.project.post.repository.PostsRepository;
import com.aivle.project.user.entity.UserEntity;
import com.aivle.project.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 게시글 CRUD 서비스.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

	private final PostsRepository postsRepository;
	private final UserRepository userRepository;
	private final CategoriesRepository categoriesRepository;

	@Transactional(readOnly = true)
	public List<PostResponse> list() {
		return postsRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc().stream()
			.map(PostResponse::from)
			.toList();
	}

	@Transactional(readOnly = true)
	public PostResponse get(Long postId) {
		PostsEntity post = findPost(postId);
		return PostResponse.from(post);
	}

	public PostResponse create(Long userId, PostCreateRequest request) {
		validateUserId(userId);
		UserEntity user = findUser(userId);
		CategoriesEntity category = findCategory(request.getCategoryId());

		PostsEntity post = PostsEntity.create(
			user,
			category,
			request.getTitle().trim(),
			request.getContent().trim(),
			false,
			PostStatus.PUBLISHED,
			userId
		);

		PostsEntity saved = postsRepository.save(post);
		return PostResponse.from(saved);
	}

	public PostResponse update(Long userId, Long postId, PostUpdateRequest request) {
		validateUserId(userId);
		PostsEntity post = findPost(postId);
		validateOwner(post, userId);

		String nextTitle = request.getTitle() != null ? request.getTitle().trim() : post.getTitle();
		String nextContent = request.getContent() != null ? request.getContent().trim() : post.getContent();
		CategoriesEntity nextCategory = post.getCategory();

		if (request.getCategoryId() != null) {
			nextCategory = findCategory(request.getCategoryId());
		}

		validatePatch(nextTitle, nextContent, request);

		post.update(nextTitle, nextContent, nextCategory, userId);
		return PostResponse.from(post);
	}

	public void delete(Long userId, Long postId) {
		validateUserId(userId);
		PostsEntity post = findPost(postId);
		validateOwner(post, userId);
		post.markDeleted(userId);
	}

	private PostsEntity findPost(Long postId) {
		return postsRepository.findByIdAndDeletedAtIsNull(postId)
			.orElseThrow(() -> new CommonException(CommonErrorCode.COMMON_404));
	}

	private UserEntity findUser(Long userId) {
		return userRepository.findByIdAndDeletedAtIsNull(userId)
			.orElseThrow(() -> new CommonException(CommonErrorCode.COMMON_404));
	}

	private CategoriesEntity findCategory(Long categoryId) {
		return categoriesRepository.findByIdAndDeletedAtIsNull(categoryId)
			.orElseThrow(() -> new CommonException(CommonErrorCode.COMMON_404));
	}

	private void validateOwner(PostsEntity post, Long userId) {
		if (!post.getUser().getId().equals(userId)) {
			throw new CommonException(CommonErrorCode.COMMON_403);
		}
	}

	private void validateUserId(Long userId) {
		if (userId == null) {
			throw new CommonException(CommonErrorCode.COMMON_400);
		}
		if (userId <= 0) {
			throw new CommonException(CommonErrorCode.COMMON_400_VALIDATION);
		}
	}

	private void validatePatch(String title, String content, PostUpdateRequest request) {
		boolean hasAnyChange = request.getTitle() != null || request.getContent() != null || request.getCategoryId() != null;
		if (!hasAnyChange) {
			throw new CommonException(CommonErrorCode.COMMON_400);
		}
		if (title != null && title.isBlank()) {
			throw new CommonException(CommonErrorCode.COMMON_400_VALIDATION);
		}
		if (content != null && content.isBlank()) {
			throw new CommonException(CommonErrorCode.COMMON_400_VALIDATION);
		}
	}
}
