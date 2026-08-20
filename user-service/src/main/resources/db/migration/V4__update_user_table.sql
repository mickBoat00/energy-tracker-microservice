ALTER TABLE users
    ADD COLUMN sub VARCHAR(255),
    ADD COLUMN name VARCHAR(255),
    DROP COLUMN first_name,
    DROP COLUMN last_name;

CREATE UNIQUE INDEX idx_users_sub ON users (sub);
