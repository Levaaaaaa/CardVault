CREATE TYPE user_role AS ENUM('USER', 'ADMIN');
CREATE TYPE card_status AS ENUM('ACTIVE', 'BLOCKED', 'EXPIRED');

CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  first_name varchar(100) NOT NULL,
  last_name varchar(100) NOT NULL,
  email varchar(100) not null unique,
  password BYTEA not null,
  role user_role NOT NULL,
  created_at TIMESTAMP DEFAULT now(),
  updated_at TIMESTAMP DEFAULT now(),
  enabled BOOLEAN NOT NULL
);

CREATE INDEX users_email_idx on users (email);

CREATE TABLE cards(
    id SERIAL PRIMARY KEY,
    card_number BYTEA NOT NULL,
    card_owner bigint NOT NULL,
    validity_period DATE NOT NULL,
    status card_status NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    CONSTRAINT fk_card_owner
        FOREIGN KEY(card_owner)
        REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE transactions(
    id SERIAL PRIMARY KEY,
    producer BIGINT NOT NULL,
    consumer BIGINT NOT NULL,
    transaction_date timestamp not null,
    amount DECIMAL(12, 2) NOT NULL CHECK(amount > 0),
    CONSTRAINT fk_consumer
        FOREIGN KEY (consumer)
        REFERENCES cards(id),
    CONSTRAINT fk_producer
        FOREIGN KEY (producer)
        REFERENCES cards(id),
);