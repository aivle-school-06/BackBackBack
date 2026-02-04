-- ============================================
-- 7. 산업 코드 도메인
-- ============================================

CREATE TABLE `industry_codes` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '산업 코드 고유 식별자',
  `industry_code` VARCHAR(5) NOT NULL COMMENT '산업 코드 (외부 기준)',
  `industry_name` VARCHAR(100) NOT NULL COMMENT '산업명',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
  `deleted_at` TIMESTAMP NULL COMMENT '삭제일시 (Soft Delete)',
  `created_by` BIGINT NULL COMMENT '생성자 ID',
  `updated_by` BIGINT NULL COMMENT '수정자 ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_industry_code` (`industry_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='산업 코드';

ALTER TABLE `companies`
  ADD COLUMN `industry_code` VARCHAR(5) NULL COMMENT '산업 코드',
  ADD INDEX `idx_companies_industry_code` (`industry_code`),
  ADD CONSTRAINT `fk_companies_industry_code`
    FOREIGN KEY (`industry_code`) REFERENCES `industry_codes`(`industry_code`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;
