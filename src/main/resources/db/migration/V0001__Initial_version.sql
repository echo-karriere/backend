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

CREATE TABLE IF NOT EXISTS company
(
    id          uuid        NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    name        text        NOT NULL UNIQUE,
    homepage    text,
    created_at  timestamptz NOT NULL             DEFAULT now(),
    modified_at timestamptz
);