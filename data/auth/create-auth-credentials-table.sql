CREATE TABLE auth.user_credentials (
    user_id uuid PRIMARY KEY,
    email text NOT NULL UNIQUE,
    hashed_password text NOT NULL,
    created_at timestamptz NOT NULL default now(),
    updated_at timestamptz NOT NULL default now()
);