package com.aivle.project.file.entity;

import com.aivle.project.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * files 테이블에 매핑되는 파일 엔티티.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "files")
public class FilesEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "usage_type", nullable = false, length = 20)
	private FileUsageType usageType;

	@Column(name = "storage_url", nullable = false, length = 500)
	private String storageUrl;

	@Column(name = "original_filename", nullable = false, length = 255)
	private String originalFilename;

	@Column(name = "file_size", nullable = false)
	private long fileSize;

	@Column(name = "content_type", nullable = false, length = 100)
	private String contentType;

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "updated_by")
	private Long updatedBy;

	public static FilesEntity create(
		FileUsageType usageType,
		String storageUrl,
		String originalFilename,
		long fileSize,
		String contentType,
		Long actorId
	) {
		FilesEntity file = new FilesEntity();
		file.usageType = usageType;
		file.storageUrl = storageUrl;
		file.originalFilename = originalFilename;
		file.fileSize = fileSize;
		file.contentType = contentType;
		file.createdBy = actorId;
		file.updatedBy = actorId;
		return file;
	}
}
