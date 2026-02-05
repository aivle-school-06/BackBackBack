ALTER TABLE companies
    ADD COLUMN industry_code_id BIGINT;

CREATE INDEX idx_companies_industry_code ON companies (industry_code_id);

ALTER TABLE companies
    ADD CONSTRAINT fk_companies_industry_code
        FOREIGN KEY (industry_code_id) REFERENCES industry_codes(id)
            ON DELETE SET NULL;
