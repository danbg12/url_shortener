CREATE TABLE IF NOT EXISTS urls (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    original_url varchar(2048) UNIQUE NOT NULL,
    short_url varchar(255) UNIQUE NOT NULL,
    created_at timestamp DEFAULT current_timestamp,
    expires_at timestamp NOT NULL,
    url_hash_code varchar(8) UNIQUE NOT NULL
);