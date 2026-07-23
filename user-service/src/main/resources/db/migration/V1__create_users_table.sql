CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    address VARCHAR(255),
    email VARCHAR(255),
    enable_alerting BOOLEAN NOT NULL DEFAULT FALSE,
    alerting_threshold INT NOT NULL
);