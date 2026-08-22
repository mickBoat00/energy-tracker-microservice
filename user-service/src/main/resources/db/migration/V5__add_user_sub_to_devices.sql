ALTER TABLE devices
    ADD COLUMN user_sub VARCHAR(255);

CREATE INDEX idx_devices_user_sub ON devices (user_sub);
