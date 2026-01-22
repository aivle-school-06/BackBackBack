-- ============================================================
-- Table: comments (댓글)
-- Description: 계층형 댓글 (Adjacency List Model)
-- ============================================================
CREATE TABLE comments (
    -- Primary Key
    id                    BIGINT          NOT NULL AUTO_INCREMENT COMMENT '댓글 고유 식별자',

    -- Foreign Keys
    post_id               BIGINT          NOT NULL COMMENT '게시글 ID',
    user_id               BIGINT          NOT NULL COMMENT '작성자 ID',
    parent_id             BIGINT          NULL COMMENT '부모 댓글 ID (NULL이면 최상위 댓글)',

    -- Content
    content               TEXT            NOT NULL COMMENT '댓글 내용',

    -- Hierarchy Management
    depth                 INT             NOT NULL DEFAULT 0 COMMENT '댓글 깊이 (0: 최상위)',
    sequence              INT             NOT NULL DEFAULT 0 COMMENT '동일 부모 내 정렬 순서',

    -- Auditing Fields
    created_at            TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at            TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    deleted_at            TIMESTAMP       NULL COMMENT '삭제일시 (Soft Delete)',
    created_by            BIGINT          NULL COMMENT '생성자 ID',
    updated_by            BIGINT          NULL COMMENT '수정자 ID',

    -- Constraints
    PRIMARY KEY (id),
    CONSTRAINT fk_comments_post FOREIGN KEY (post_id) REFERENCES posts (id),
    CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_comments_parent FOREIGN KEY (parent_id) REFERENCES comments (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='댓글';

-- Indexes
CREATE INDEX idx_comments_post_id ON comments (post_id);
CREATE INDEX idx_comments_user_id ON comments (user_id);
CREATE INDEX idx_comments_parent_id ON comments (parent_id);
CREATE INDEX idx_comments_created_at ON comments (created_at DESC);
CREATE INDEX idx_comments_deleted_at ON comments (deleted_at);
-- 복합 인덱스: 게시글별 댓글 정렬 최적화
CREATE INDEX idx_comments_post_depth_seq ON comments (post_id, depth, sequence);
