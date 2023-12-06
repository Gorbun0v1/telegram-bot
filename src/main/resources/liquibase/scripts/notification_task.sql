CREATE TABLE notification_task(
    id BIGSERIAL,
    chat_id BIGINT,
    message TEXT,
    time TIMESTAMP)