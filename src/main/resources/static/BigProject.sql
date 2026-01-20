CREATE TABLE `post_tags` (
	`id`	BIGINT	NOT NULL	COMMENT '매핑 고유 식별자',
	`post_id`	BIGINT	NOT NULL	COMMENT '게시글 고유 식별자',
	`tag_id`	BIGINT	NOT NULL	COMMENT '태그 ID',
	`created_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '생성일시'
);

CREATE TABLE `files` (
	`id`	BIGINT	NOT NULL	COMMENT '파일 고유 식별자',
	`post_id`	BIGINT	NOT NULL	COMMENT '게시글 고유 식별자',
	`storage_url`	VARCHAR(500)	NOT NULL	COMMENT '저장소 URL (S3 등)',
	`original_filename`	VARCHAR(255)	NOT NULL	COMMENT '원본 파일명',
	`file_size`	BIGINT	NOT NULL	COMMENT '파일 크기 (bytes)',
	`content_type`	VARCHAR(100)	NOT NULL	COMMENT 'MIME 타입 (예: image/png)',
	`created_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '생성일시',
	`updated_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '수정일시',
	`deleted_at`	TIMESTAMP	NULL	COMMENT '삭제일시 (Soft Delete)',
	`created_by`	BIGINT	NULL	COMMENT '생성자 ID',
	`updated_by`	BIGINT	NULL	COMMENT '수정자 ID'
);

CREATE TABLE `comments` (
	`id`	BIGINT	NOT NULL	COMMENT '댓글 고유 식별자',
	`parent_id`	BIGINT	NULL	COMMENT '부모 댓글 ID (NULL이면 최상위 댓글)',
	`post_id`	BIGINT	NOT NULL	COMMENT '게시글 고유 식별자',
	`user_id`	BIGINT	NOT NULL	COMMENT '사용자 고유 식별자',
	`content`	TEXT	NOT NULL	COMMENT '댓글 내용',
	`depth`	INT	NOT NULL	DEFAULT 0	COMMENT '댓글 깊이 (0: 최상위)',
	`sequence`	INT	NOT NULL	DEFAULT 0	COMMENT '동일 부모 내 정렬 순서',
	`created_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '생성일시',
	`updated_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '수정일시',
	`deleted_at`	TIMESTAMP	NULL	COMMENT '삭제일시 (Soft Delete)',
	`created_by`	BIGINT	NULL	COMMENT '생성자 ID',
	`updated_by`	BIGINT	NULL	COMMENT '수정자 ID'
);

CREATE TABLE `roles` (
	`id`	BIGINT	NOT NULL	COMMENT '역할 고유 식별자',
	`name`	VARCHAR(50)	NOT NULL	COMMENT '역할명 (ROLE_USER, ROLE_ADMIN, ROLE_MANAGER)',
	`description`	VARCHAR(200)	NULL	COMMENT '역할 설명',
	`created_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '생성일시',
	`updated_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '수정일시',
	`deleted_at`	TIMESTAMP	NULL	COMMENT '삭제일시 (Soft Delete)',
	`created_by`	BIGINT	NULL	COMMENT '생성자 ID',
	`updated_by`	BIGINT	NULL	COMMENT '수정자 ID'
);

CREATE TABLE `filing_summary` (
	`id`	BIGINT	NULL,
	`companies_id`	BIGINT	NOT NULL,
	`account_master_id`	BIGINT	NOT NULL,
	`bsns_year`	SMALLINT	NOT NULL,
	`reprt_code`	CHAR(5)	NOT NULL,
	`fs_div`	CHAR(3)	NOT NULL,
	`total_amount`	DECIMAL(22,0)	NULL,
	`avg_amount`	DECIMAL(22,2)	NULL,
	`min_amount`	DECIMAL(22,0)	NULL,
	`max_amount`	DECIMAL(22,0)	NULL,
	`record_count`	INT	NOT NULL,
	`last_updated`	DATETIME	NOT NULL
);

CREATE TABLE `post_likes` (
	`id`	BIGINT	NOT NULL	COMMENT '좋아요 고유 식별자',
	`post_id`	BIGINT	NOT NULL	COMMENT '게시글 고유 식별자',
	`user_id`	BIGINT	NOT NULL	COMMENT '사용자 고유 식별자',
	`created_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '생성일시'
);

CREATE TABLE `companies` (
	`id`	BIGINT	NULL,
	`corp_code`	CHAR(8)	NOT NULL,
	`corp_name`	VARCHAR(100)	NOT NULL,
	`corp_name_eng`	VARCHAR(200)	NULL,
	`stock_name`	VARCHAR(100)	NULL,
	`stock_code`	CHAR(6)	NULL,
	`ceo_nm`	VARCHAR(100)	NULL,
	`corp_cls`	CHAR(1)	NOT NULL,
	`jurir_no`	VARCHAR(20)	NULL,
	`bizr_no`	VARCHAR(20)	NULL,
	`induty_code`	VARCHAR(10)	NULL,
	`est_dt`	DATE	NULL,
	`acc_mt`	TINYINT	NULL,
	`adres`	VARCHAR(500)	NULL,
	`hm_url`	VARCHAR(500)	NULL,
	`ir_url`	VARCHAR(500)	NULL,
	`phn_no`	VARCHAR(30)	NULL,
	`fax_no`	VARCHAR(30)	NULL,
	`last_updated`	DATETIME	NULL
);

CREATE TABLE `data_load_logs` (
	`id`	BIGINT	NULL,
	`companies_id`	BIGINT	NOT NULL,
	`bsns_year`	SMALLINT	NOT NULL,
	`reprt_code`	CHAR(5)	NOT NULL,
	`fs_div`	CHAR(3)	NOT NULL,
	`status`	ENUM('SUCCESS','FAILED','PARTIAL')	NOT NULL,
	`records_loaded`	INT	NULL,
	`error_message`	TEXT	NULL,
	`started_at`	DATETIME	NOT NULL,
	`completed_at`	DATETIME	NULL
);

CREATE TABLE `tags` (
	`id`	BIGINT	NOT NULL	COMMENT '태그 고유 식별자',
	`name`	VARCHAR(50)	NOT NULL	COMMENT '태그명',
	`created_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '생성일시',
	`updated_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '수정일시',
	`deleted_at`	TIMESTAMP	NULL	COMMENT '삭제일시 (Soft Delete)',
	`created_by`	BIGINT	NULL	COMMENT '생성자 ID',
	`updated_by`	BIGINT	NULL	COMMENT '수정자 ID'
);

CREATE TABLE `posts` (
	`id`	BIGINT	NOT NULL	COMMENT '게시글 고유 식별자',
	`user_id`	BIGINT	NOT NULL	COMMENT '사용자 고유 식별자',
	`category_id`	BIGINT	NOT NULL	COMMENT '카테고리 고유 식별자',
	`title`	VARCHAR(200)	NOT NULL	COMMENT '게시글 제목',
	`content`	LONGTEXT	NOT NULL	COMMENT '게시글 내용',
	`view_count`	INT	NOT NULL	DEFAULT 0	COMMENT '조회수',
	`is_pinned`	TINYINT(1)	NOT NULL	DEFAULT 0	COMMENT '공지 여부 (0: 일반, 1: 공지)',
	`status`	VARCHAR(20)	NOT NULL	DEFAULT 'PUBLISHED'	COMMENT '게시 상태 (DRAFT, PUBLISHED, HIDDEN)',
	`created_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '생성일시',
	`updated_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '수정일시',
	`deleted_at`	TIMESTAMP	NULL	COMMENT '삭제일시 (Soft Delete)',
	`created_by`	BIGINT	NULL	COMMENT '생성자 ID',
	`updated_by`	BIGINT	NULL	COMMENT '수정자 ID'
);

CREATE TABLE `user_roles` (
	`id`	BIGINT	NOT NULL	COMMENT '매핑 고유 식별자',
	`user_id`	BIGINT	NOT NULL	COMMENT '사용자 고유 식별자',
	`role_id`	BIGINT	NOT NULL	COMMENT '역할 ID',
	`created_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '생성일시'
);

CREATE TABLE `filing_master` (
	`id`	BIGINT	NULL,
	`rcept_no`	CHAR(14)	NOT NULL,
	`companies_id`	BIGINT	NOT NULL,
	`bsns_year`	SMALLINT	NOT NULL,
	`reprt_code`	CHAR(5)	NOT NULL,
	`fs_div`	CHAR(3)	NOT NULL,
	`thstrm_nm`	VARCHAR(50)	NULL,
	`frmtrm_nm`	VARCHAR(50)	NULL,
	`bfefrmtrm_nm`	VARCHAR(50)	NULL,
	`created_at`	DATETIME	NOT NULL,
	`updated_at`	DATETIME	NOT NULL	DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `refresh_tokens` (
	`id`	BIGINT	NOT NULL	COMMENT '토큰 고유 식별자',
	`user_id`	BIGINT	NOT NULL	COMMENT '사용자 고유 식별자',
	`token_value`	VARCHAR(512)	NOT NULL	COMMENT '리프레시 토큰 값',
	`device_info`	VARCHAR(500)	NULL	COMMENT '디바이스 정보 (User-Agent)',
	`ip_address`	VARCHAR(45)	NULL	COMMENT 'IP 주소 (IPv6 지원)',
	`expires_at`	TIMESTAMP	NOT NULL	COMMENT '토큰 만료일시',
	`is_revoked`	TINYINT(1)	NOT NULL	DEFAULT 0	COMMENT '폐기 여부 (0: 유효, 1: 폐기)',
	`created_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '생성일시',
	`updated_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '수정일시'
);

CREATE TABLE `account_master` (
	`id`	BIGINT	NULL,
	`account_id`	VARCHAR(100)	NULL,
	`account_nm`	VARCHAR(150)	NOT NULL,
	`account_detail`	VARCHAR(500)	NULL,
	`sj_div`	VARCHAR(10)	NOT NULL,
	`account_master_id`	BIGINT	NULL,
	`depth`	TINYINT	NOT NULL,
	`is_total`	BOOLEAN	NOT NULL,
	`sort_order`	INT	NOT NULL,
	`is_active`	BOOLEAN	NOT NULL,
	`created_at`	DATETIME	NOT NULL,
	`updated_at`	DATETIME	NOT NULL	DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `filing_detail` (
	`filing_master_id`	BIGINT	NOT NULL,
	`bsns_year`	SMALLINT	NOT NULL,
	`ord`	MEDIUMINT	NOT NULL,
	`account_master_id`	BIGINT	NOT NULL,
	`currency`	CHAR(3)	NULL	DEFAULT 'KRW',
	`thstrm_amount`	DECIMAL(20,0)	NULL,
	`thstrm_add_amount`	DECIMAL(20,0)	NULL,
	`frmtrm_amount`	DECIMAL(20,0)	NULL,
	`frmtrm_q_amount`	DECIMAL(20,0)	NULL,
	`frmtrm_add_amount`	DECIMAL(20,0)	NULL,
	`bfefrmtrm_amount`	DECIMAL(20,0)	NULL,
	`created_at`	DATETIME	NOT NULL,
	`PARTITION`	p2020	NULL,
	`PARTITION`	p2021	NULL,
	`PARTITION`	p2022	NULL,
	`PARTITION`	p2023	NULL,
	`PARTITION`	p2024	NULL,
	`PARTITION`	p2025	NULL,
	`PARTITION`	p2026	NULL,
	`PARTITION`	p_future	NULL
);

CREATE TABLE `users` (
	`id`	BIGINT	NOT NULL	COMMENT '사용자 고유 식별자',
	`company_id`	BIGINT	NULL,
	`uuid`	BINARY(16)	NOT NULL	COMMENT '외부 API 노출용 UUID',
	`email`	VARCHAR(100)	NOT NULL	COMMENT '이메일 (로그인 ID)',
	`password`	VARCHAR(255)	NOT NULL	COMMENT '비밀번호 (BCrypt 해시)',
	`name`	VARCHAR(50)	NOT NULL	COMMENT '사용자 이름',
	`phone`	VARCHAR(20)	NULL	COMMENT '연락처',
	`status`	VARCHAR(20)	NOT NULL	DEFAULT 'PENDING'	COMMENT '계정 상태 (PENDING, ACTIVE, INACTIVE, BANNED)',
	`created_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '생성일시',
	`updated_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '수정일시',
	`deleted_at`	TIMESTAMP	NULL	COMMENT '삭제일시 (Soft Delete)',
	`created_by`	BIGINT	NULL	COMMENT '생성자 ID',
	`updated_by`	BIGINT	NULL	COMMENT '수정자 ID'
);

CREATE TABLE `categories` (
	`id`	BIGINT	NOT NULL	COMMENT '카테고리 고유 식별자',
	`name`	VARCHAR(50)	NOT NULL	COMMENT '카테고리명',
	`description`	VARCHAR(200)	NULL	COMMENT '카테고리 설명',
	`sort_order`	INT	NOT NULL	DEFAULT 0	COMMENT '정렬 순서',
	`is_active`	TINYINT(1)	NOT NULL	DEFAULT 1	COMMENT '활성화 여부 (0: 비활성, 1: 활성)',
	`created_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '생성일시',
	`updated_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '수정일시',
	`deleted_at`	TIMESTAMP	NULL	COMMENT '삭제일시 (Soft Delete)',
	`created_by`	BIGINT	NULL	COMMENT '생성자 ID',
	`updated_by`	BIGINT	NULL	COMMENT '수정자 ID'
);

CREATE TABLE `common_codes` (
	`group_code`	VARCHAR(20)	NOT NULL,
	`code`	VARCHAR(20)	NOT NULL,
	`code_name`	VARCHAR(100)	NOT NULL,
	`description`	VARCHAR(255)	NULL,
	`sort_order`	SMALLINT	NOT NULL,
	`is_active`	BOOLEAN	NOT NULL,
	`created_at`	DATETIME	NOT NULL,
	`updated_at`	DATETIME	NOT NULL	DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE `post_tags` ADD CONSTRAINT `PK_POST_TAGS` PRIMARY KEY (
	`id`
);

ALTER TABLE `files` ADD CONSTRAINT `PK_FILES` PRIMARY KEY (
	`id`
);

ALTER TABLE `comments` ADD CONSTRAINT `PK_COMMENTS` PRIMARY KEY (
	`id`
);

ALTER TABLE `roles` ADD CONSTRAINT `PK_ROLES` PRIMARY KEY (
	`id`
);

ALTER TABLE `post_likes` ADD CONSTRAINT `PK_POST_LIKES` PRIMARY KEY (
	`id`
);

ALTER TABLE `companies` ADD CONSTRAINT `PK_COMPANIES` PRIMARY KEY (
	`id`
);

ALTER TABLE `tags` ADD CONSTRAINT `PK_TAGS` PRIMARY KEY (
	`id`
);

ALTER TABLE `posts` ADD CONSTRAINT `PK_POSTS` PRIMARY KEY (
	`id`
);

ALTER TABLE `user_roles` ADD CONSTRAINT `PK_USER_ROLES` PRIMARY KEY (
	`id`
);

ALTER TABLE `filing_master` ADD CONSTRAINT `PK_FILING_MASTER` PRIMARY KEY (
	`id`
);

ALTER TABLE `refresh_tokens` ADD CONSTRAINT `PK_REFRESH_TOKENS` PRIMARY KEY (
	`id`
);

ALTER TABLE `account_master` ADD CONSTRAINT `PK_ACCOUNT_MASTER` PRIMARY KEY (
	`id`
);

ALTER TABLE `filing_detail` ADD CONSTRAINT `PK_FILING_DETAIL` PRIMARY KEY (
	`filing_master_id`,
	`bsns_year`,
	`ord`
);

ALTER TABLE `users` ADD CONSTRAINT `PK_USERS` PRIMARY KEY (
	`id`
);

ALTER TABLE `categories` ADD CONSTRAINT `PK_CATEGORIES` PRIMARY KEY (
	`id`
);

ALTER TABLE `common_codes` ADD CONSTRAINT `PK_COMMON_CODES` PRIMARY KEY (
	`group_code`,
	`code`
);

