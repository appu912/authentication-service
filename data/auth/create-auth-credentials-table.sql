CREATE TABLE auth.user_credentials (
    user_id uuid PRIMARY KEY,
    email varchar(255) NOT NULL UNIQUE,
    hashed_password varchar(255) NOT NULL,
    created_at timestamptz NOT NULL default now(),
    updated_at timestamptz NOT NULL default now()
);