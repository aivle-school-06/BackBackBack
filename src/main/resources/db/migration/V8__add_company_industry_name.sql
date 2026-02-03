ALTER TABLE `companies`
  ADD COLUMN `industry_name` VARCHAR(100) NULL COMMENT '업종명' AFTER `corp_eng_name`;
