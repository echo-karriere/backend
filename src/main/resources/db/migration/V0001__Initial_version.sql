create extension if not exists "pgcrypto";

create table if not exists category
(
    id          uuid        not null primary key default gen_random_uuid(),
    title       text        not null unique,
    description text        not null,
    slug        text        not null unique,
    created_at  timestamptz not null             default now(),
    modified_at timestamptz
);

create table if not exists company
(
    id          uuid        not null primary key default gen_random_uuid(),
    name        text        not null unique,
    homepage    text,
    created_at  timestamptz not null             default now(),
    modified_at timestamptz
);

create type UserType as ENUM ('ADMIN', 'STAFF', 'USER');

create table if not exists "user"
(
    id          uuid        not null primary key default gen_random_uuid(),
    name        text        not null,
    email       text        not null unique,
    password    text        not null,
    active      boolean     not null,
    type        UserType    not null,
    created_at  timestamptz not null             default now(),
    modified_at timestamptz
);

create table if not exists refresh_token
(
    user_id       uuid        not null primary key,
    refresh_token text        not null,
    expires_at    timestamptz,
    created_at    timestamptz not null default now(),
    foreign key (user_id) references "user" (id) on update restrict on delete cascade
)
