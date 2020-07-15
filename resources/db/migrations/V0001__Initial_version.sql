CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS namespace
(
    id          uuid NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    title       text NOT NULL UNIQUE,
    description text NOT NULL,
    namespace   text NOT NULL UNIQUE
)