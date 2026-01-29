package com.aivle.project.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * refresh_tokens 테이블 엔티티.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "refresh_tokens")
public class RefreshTokenEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "token_value", nullable = false, length = 512, unique = true)
	private String tokenValue;

	@Column(name = "device_info", length = 500)
	private String deviceInfo;

	@Column(name = "ip_address", length = 45)
	private String ipAddress;

	@Column(name = "expires_at", nullable = false)
	private LocalDateTime expiresAt;

	@Column(name = "is_revoked", nullable = false)
	private boolean revoked;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	public RefreshTokenEntity(
		Long userId,
		String tokenValue,
		String deviceInfo,
		String ipAddress,
		LocalDateTime expiresAt
	) {
		this.userId = userId;
		this.tokenValue = tokenValue;
		this.deviceInfo = deviceInfo;
		this.ipAddress = ipAddress;
		this.expiresAt = expiresAt;
		this.revoked = false;
	}

	public void revoke() {
		if (revoked) {
			return;
		}
		revoked = true;
	}
}
