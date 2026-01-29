package com.aivle.project.post.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * post_view_counts 테이블에 매핑되는 조회수 엔티티.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "post_view_counts")
public class PostViewCountsEntity {

	@Id
	@Column(name = "post_id")
	private Long postId;

	@MapsId
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "post_id", nullable = false)
	private PostsEntity post;

	@Column(name = "view_count", nullable = false)
	private int viewCount = 0;

	public static PostViewCountsEntity create(PostsEntity post) {
		PostViewCountsEntity viewCounts = new PostViewCountsEntity();
		viewCounts.post = post;
		viewCounts.viewCount = 0;
		return viewCounts;
	}

	public void increase() {
		this.viewCount += 1;
	}
}
