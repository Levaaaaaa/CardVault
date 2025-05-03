CREATE TYPE user_role AS ENUM('USER', 'ADMIN');

CREATE TYPE card_status AS ENUM('ACTIVE', 'BLOCKED', 'EXPIRED');

CREATE TABLE users (
  id UUID PRIMARY KEY,
  first_name varchar(100) NOT NULL,
  last_name varchar(100) NOT NULL,
  email varchar(100) not null unique,
  password VARCHAR(64) not null,
  role user_role NOT NULL,
  created_at TIMESTAMP DEFAULT now(),
  updated_at TIMESTAMP DEFAULT now(),
  enabled BOOLEAN NOT NULL
);

CREATE INDEX users_email_idx on users (email);

CREATE TABLE cards (
  id UUID PRIMARY KEY,
  card_number BYTEA NOT NULL UNIQUE,
  card_number_hash BYTEA NOT NULL UNIQUE,
  card_owner UUID NOT NULL,
  validity_period DATE NOT NULL,
  status card_status NOT NULL,
  balance DECIMAL(19, 2) NOT NULL,
  created_at TIMESTAMP DEFAULT now(),
  CONSTRAINT fk_card_owner FOREIGN KEY (card_owner) REFERENCES users (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX card_number_hash_idx ON cards (card_number_hash);

CREATE TABLE transactions (
  id UUID PRIMARY KEY,
  producer UUID NOT NULL,
  consumer UUID NOT NULL,
  transaction_date timestamp not null,
  amount DECIMAL(12, 2) NOT NULL CHECK (amount > 0),
  CONSTRAINT fk_consumer FOREIGN KEY (consumer) REFERENCES cards (id),
  CONSTRAINT fk_producer FOREIGN KEY (producer) REFERENCES cards (id)
);