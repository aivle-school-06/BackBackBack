package com.aivle.project.file.repository;

import com.aivle.project.file.entity.FilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 파일 저장소.
 */
public interface FilesRepository extends JpaRepository<FilesEntity, Long> {
}
