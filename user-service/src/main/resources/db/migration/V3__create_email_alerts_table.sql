CREATE TABLE EmailAlerts (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    message    TEXT NOT NULL,
    sent       BOOLEAN NOT NULL DEFAULT FALSE,
    timestamp  DATETIME NOT NULL
);

CREATE INDEX idx_email_alerts_user_id_sent_timestamp
    ON EmailAlerts (user_id, sent, timestamp);