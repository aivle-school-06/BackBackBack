package com.aivle.project.post.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aivle.project.category.entity.CategoriesEntity;
import com.aivle.project.common.config.TestSecurityConfig;
import com.aivle.project.post.dto.PostCreateRequest;
import com.aivle.project.post.dto.PostResponse;
import com.aivle.project.post.dto.PostUpdateRequest;
import com.aivle.project.post.entity.PostStatus;
import com.aivle.project.post.entity.PostsEntity;
import com.aivle.project.user.entity.UserEntity;
import com.aivle.project.user.entity.UserStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@Transactional
@Import(TestSecurityConfig.class)
class PostControllerTest {

	private static final String USER_ID_HEADER = "X-User-Id";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	@DisplayName("게시글을 생성하고 조회한다")
	void createAndGetPost() throws Exception {
		// given: 사용자/카테고리를 준비
		UserEntity user = givenUser("user-create@test.com", UserStatus.ACTIVE);
		CategoriesEntity category = givenCategory("general");
		entityManager.persist(user);
		entityManager.persist(category);
		entityManager.flush();

		PostCreateRequest request = new PostCreateRequest();
		request.setCategoryId(category.getId());
		request.setTitle("hello");
		request.setContent("content");

		// when: 게시글 생성 요청
		MvcResult createResult = mockMvc.perform(post("/User/posts")
				.header(USER_ID_HEADER, user.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andReturn();

		PostResponse created = objectMapper.readValue(createResult.getResponse().getContentAsString(), PostResponse.class);

		// then: 조회 시 동일한 게시글이 반환된다
		MvcResult getResult = mockMvc.perform(get("/User/posts/{id}", created.id()))
			.andExpect(status().isOk())
			.andReturn();

		PostResponse found = objectMapper.readValue(getResult.getResponse().getContentAsString(), PostResponse.class);
		assertThat(found.id()).isEqualTo(created.id());
		assertThat(found.userId()).isEqualTo(user.getId());
		assertThat(found.categoryId()).isEqualTo(category.getId());
		assertThat(found.title()).isEqualTo("hello");
		assertThat(found.content()).isEqualTo("content");
	}

	@Test
	@DisplayName("작성자는 게시글을 수정할 수 있다")
	void updatePostByOwner() throws Exception {
		// given: 사용자/카테고리/게시글을 준비
		UserEntity user = givenUser("user-update@test.com", UserStatus.ACTIVE);
		CategoriesEntity category = givenCategory("general");
		entityManager.persist(user);
		entityManager.persist(category);
		PostsEntity post = givenPost(user, category, "before", "content", PostStatus.PUBLISHED);
		entityManager.persist(post);
		entityManager.flush();

		PostUpdateRequest request = new PostUpdateRequest();
		request.setTitle("after");

		// when: 수정 요청
		MvcResult updateResult = mockMvc.perform(patch("/User/posts/{id}", post.getId())
				.header(USER_ID_HEADER, user.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andReturn();

		// then: 제목이 변경된다
		PostResponse updated = objectMapper.readValue(updateResult.getResponse().getContentAsString(), PostResponse.class);
		assertThat(updated.title()).isEqualTo("after");
	}

	@Test
	@DisplayName("작성자가 아니면 게시글 수정이 거부된다")
	void updatePostByOtherUserShouldFail() throws Exception {
		// given: 게시글과 다른 사용자
		UserEntity owner = givenUser("owner@test.com", UserStatus.ACTIVE);
		UserEntity other = givenUser("other@test.com", UserStatus.ACTIVE);
		CategoriesEntity category = givenCategory("general");
		entityManager.persist(owner);
		entityManager.persist(other);
		entityManager.persist(category);
		PostsEntity post = givenPost(owner, category, "title", "content", PostStatus.PUBLISHED);
		entityManager.persist(post);
		entityManager.flush();

		PostUpdateRequest request = new PostUpdateRequest();
		request.setTitle("forbidden");

		// when: 다른 사용자가 수정 요청
		mockMvc.perform(patch("/User/posts/{id}", post.getId())
				.header(USER_ID_HEADER, other.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			// then: 403 응답
			.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("작성자는 게시글을 삭제할 수 있고 삭제된 글은 조회되지 않는다")
	void deletePostByOwner() throws Exception {
		// given: 사용자/게시글 준비
		UserEntity user = givenUser("user-delete@test.com", UserStatus.ACTIVE);
		CategoriesEntity category = givenCategory("general");
		entityManager.persist(user);
		entityManager.persist(category);
		PostsEntity post = givenPost(user, category, "title", "content", PostStatus.PUBLISHED);
		entityManager.persist(post);
		entityManager.flush();

		// when: 삭제 요청
		mockMvc.perform(delete("/User/posts/{id}", post.getId())
				.header(USER_ID_HEADER, user.getId()))
			.andExpect(status().isNoContent());

		// then: 조회 시 404
		mockMvc.perform(get("/User/posts/{id}", post.getId()))
			.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("삭제된 게시글은 목록에서 제외된다")
	void listShouldExcludeDeletedPosts() throws Exception {
		// given: 사용자/카테고리/게시글 두 건
		UserEntity user = givenUser("user-list@test.com", UserStatus.ACTIVE);
		CategoriesEntity category = givenCategory("general");
		entityManager.persist(user);
		entityManager.persist(category);
		PostsEntity kept = givenPost(user, category, "keep", "content", PostStatus.PUBLISHED);
		PostsEntity deleted = givenPost(user, category, "delete", "content", PostStatus.PUBLISHED);
		entityManager.persist(kept);
		entityManager.persist(deleted);
		entityManager.flush();

		// when: 하나를 삭제
		mockMvc.perform(delete("/User/posts/{id}", deleted.getId())
				.header(USER_ID_HEADER, user.getId()))
			.andExpect(status().isNoContent());

		// then: 목록에는 남은 글만 존재
		MvcResult listResult = mockMvc.perform(get("/User/posts"))
			.andExpect(status().isOk())
			.andReturn();

		List<PostResponse> responses = objectMapper.readValue(
			listResult.getResponse().getContentAsString(),
			objectMapper.getTypeFactory().constructCollectionType(List.class, PostResponse.class)
		);
		assertThat(responses).extracting(PostResponse::id).contains(kept.getId());
		assertThat(responses).extracting(PostResponse::id).doesNotContain(deleted.getId());
	}

	private UserEntity givenUser(String email, UserStatus status) {
		UserEntity user = newEntity(UserEntity.class);
		setField(user, "email", email);
		setField(user, "password", "encoded-password");
		setField(user, "name", "test-user");
		setField(user, "status", status);
		return user;
	}

	private CategoriesEntity givenCategory(String name) {
		CategoriesEntity category = newEntity(CategoriesEntity.class);
		setField(category, "name", name);
		return category;
	}

	private PostsEntity givenPost(UserEntity user, CategoriesEntity category, String title, String content, PostStatus status) {
		PostsEntity post = newEntity(PostsEntity.class);
		setField(post, "user", user);
		setField(post, "category", category);
		setField(post, "title", title);
		setField(post, "content", content);
		setField(post, "status", status);
		return post;
	}

	private <T> T newEntity(Class<T> type) {
		try {
			var ctor = type.getDeclaredConstructor();
			ctor.setAccessible(true);
			return ctor.newInstance();
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("Failed to create " + type.getSimpleName(), e);
		}
	}

	private void setField(Object target, String fieldName, Object value) {
		ReflectionTestUtils.setField(target, fieldName, value);
	}
}
