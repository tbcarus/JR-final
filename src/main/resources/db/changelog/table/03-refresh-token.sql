--liquibase formatted sql

-- changeset author:03-refresh-token
CREATE TABLE IF NOT EXISTS refresh_token
(
    id        INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    token     VARCHAR(255) NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    expires   TIMESTAMP    NOT NULL,
    revoked   BOOLEAN DEFAULT FALSE
);
CREATE INDEX refresh_token_user ON refresh_token (user_name);