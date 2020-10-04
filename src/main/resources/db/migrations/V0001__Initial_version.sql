CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS category
(
    id          uuid        NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    title       text        NOT NULL UNIQUE,
    description text        NOT NULL,
    slug        text        NOT NULL UNIQUE,
    created_at  timestamptz NOT NULL             DEFAULT now(),
    modified_at timestamptz
);

CREATE TYPE UserType AS ENUM ('ADMIN', 'STAFF', 'USER');

CREATE TABLE IF NOT EXISTS "user"
(
    id          uuid        NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    name        text        NOT NULL,
    email       text        NOT NULL UNIQUE,
    password    text        NOT NULL,
    active      boolean     NOT NULL,
    type        userType    NOT NULL,
    created_at  timestamptz NOT NULL             DEFAULT now(),
    modified_at timestamptz
);

CREATE TABLE IF NOT EXISTS refresh_token
(
    user_id       uuid        NOT NULL PRIMARY KEY,
    refresh_token text        NOT NULL,
    expires_at    timestamptz NOT NULL,
    created_at    timestamptz NOT NULL DEFAULT now(),
    FOREIGN KEY (user_id) REFERENCES "user" (id) ON UPDATE RESTRICT ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS form
(
    id          uuid        NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    title       text        NOT NULL UNIQUE,
    description text        NOT NULL,
    created_at  timestamptz NOT NULL             DEFAULT now(),
    modified_at timestamptz
);

CREATE TABLE IF NOT EXISTS form_submission
(
    id           uuid        NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    form         uuid        NOT NULL,
    data         jsonb       NOT NULL,
    submitted_at timestamptz NOT NULL             DEFAULT now(),
    modified_at  timestamptz,
    FOREIGN KEY (form) REFERENCES form ON DELETE CASCADE
);
