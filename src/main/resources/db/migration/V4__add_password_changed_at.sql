ALTER TABLE users ADD COLUMN password_changed_at DATETIME;
UPDATE users SET password_changed_at = created_at WHERE password_changed_at IS NULL;
