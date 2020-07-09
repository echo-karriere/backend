CREATE TABLE IF NOT EXISTS namespace
(
    id          serial  NOT NULL PRIMARY KEY,
    title       text    NOT NULL UNIQUE,
    description text    NOT NULL,
    namespace   text    NOT NULL UNIQUE
)