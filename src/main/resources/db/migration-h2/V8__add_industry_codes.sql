-- H2 전용 산업 코드 스키마

CREATE TABLE industry_codes (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  industry_code VARCHAR(5) NOT NULL,
  industry_name VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP,
  created_by BIGINT,
  updated_by BIGINT,
  CONSTRAINT uk_industry_code UNIQUE (industry_code)
);

ALTER TABLE companies ADD COLUMN industry_code VARCHAR(5);
ALTER TABLE companies
  ADD CONSTRAINT fk_companies_industry_code
  FOREIGN KEY (industry_code) REFERENCES industry_codes(industry_code);
CREATE INDEX idx_companies_industry_code ON companies(industry_code);
