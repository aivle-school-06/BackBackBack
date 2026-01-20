package com.aivle.project.post.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.aivle.project.category.entity.CategoriesEntity;
import com.aivle.project.user.entity.UserEntity;
import com.aivle.project.user.entity.UserStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class PostsEntityTest {

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	@DisplayName("posts는 user/category 연관관계를 저장하고 조회할 수 있다")
	void save_mapsUserAndCategory() {
		UserEntity user = createUser("post-owner@test.com", UserStatus.ACTIVE);
		CategoriesEntity category = createCategory("general");
		entityManager.persist(user);
		entityManager.persist(category);

		PostsEntity post = createPost(user, category);
		entityManager.persist(post);
		entityManager.flush();
		entityManager.clear();

		PostsEntity found = entityManager.find(PostsEntity.class, post.getId());

		assertThat(found.getUser().getId()).isEqualTo(user.getId());
		assertThat(found.getCategory().getId()).isEqualTo(category.getId());
		assertThat(found.getTitle()).isEqualTo("test title");
		assertThat(found.getContent()).isEqualTo("test content");
	}

	@Test
	@DisplayName("posts 기본값과 상태 기본값이 저장 시 반영된다")
	void save_appliesDefaults() {
		UserEntity user = createUser("defaults@test.com", UserStatus.ACTIVE);
		CategoriesEntity category = createCategory("defaults");
		entityManager.persist(user);
		entityManager.persist(category);

		PostsEntity post = createPost(user, category);
		ReflectionTestUtils.setField(post, "status", null);

		entityManager.persist(post);
		entityManager.flush();
		entityManager.clear();

		PostsEntity found = entityManager.find(PostsEntity.class, post.getId());

		assertThat(found.getViewCount()).isZero();
		assertThat(found.isPinned()).isFalse();
		assertThat(found.getStatus()).isEqualTo(PostStatus.PUBLISHED);
	}

	private UserEntity createUser(String email, UserStatus status) {
		UserEntity user = newUserEntity();
		ReflectionTestUtils.setField(user, "email", email);
		ReflectionTestUtils.setField(user, "password", "encoded-password");
		ReflectionTestUtils.setField(user, "name", "test-user");
		ReflectionTestUtils.setField(user, "status", status);
		return user;
	}

	private UserEntity newUserEntity() {
		try {
			var ctor = UserEntity.class.getDeclaredConstructor();
			ctor.setAccessible(true);
			return ctor.newInstance();
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("Failed to create UserEntity", e);
		}
	}

	private CategoriesEntity createCategory(String name) {
		CategoriesEntity category = newCategoriesEntity();
		ReflectionTestUtils.setField(category, "name", name);
		return category;
	}

	private CategoriesEntity newCategoriesEntity() {
		try {
			var ctor = CategoriesEntity.class.getDeclaredConstructor();
			ctor.setAccessible(true);
			return ctor.newInstance();
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("Failed to create CategoriesEntity", e);
		}
	}

	private PostsEntity createPost(UserEntity user, CategoriesEntity category) {
		PostsEntity post = newPostsEntity();
		ReflectionTestUtils.setField(post, "user", user);
		ReflectionTestUtils.setField(post, "category", category);
		ReflectionTestUtils.setField(post, "title", "test title");
		ReflectionTestUtils.setField(post, "content", "test content");
		return post;
	}

	private PostsEntity newPostsEntity() {
		try {
			var ctor = PostsEntity.class.getDeclaredConstructor();
			ctor.setAccessible(true);
			return ctor.newInstance();
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("Failed to create PostsEntity", e);
		}
	}
}
