ALTER TABLE `companies`
    ADD COLUMN `industry_code_id` BIGINT NULL COMMENT '산업 코드 ID' AFTER `stock_code`;

ALTER TABLE `companies`
    ADD INDEX `idx_companies_industry_code` (`industry_code_id`);

ALTER TABLE `companies`
    ADD CONSTRAINT `fk_companies_industry_code`
        FOREIGN KEY (`industry_code_id`) REFERENCES `industry_codes`(`id`)
            ON DELETE SET NULL;
