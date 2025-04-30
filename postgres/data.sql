-- Вставка пользователей
INSERT INTO users (first_name, last_name, email, password, role, created_at, updated_at, enabled) VALUES
('Alice', 'Smith', 'alice@example.com', 'hashed_pw_1', 'USER', now(), now(), true),
('Bob', 'Johnson', 'bob@example.com', 'hashed_pw_2', 'USER', now(), now(), true),
('Charlie', 'Admin', 'admin@example.com', 'hashed_pw_admin', 'ADMIN', now(), now(), true);

-- Вставка карт (зашифрованный номер симулируется через HEX)
INSERT INTO cards (card_number, card_owner, validity_period, status) VALUES
(decode('a1b2c3d4e5f6', 'hex'), 1, '2026-12-31', 'ACTIVE'),
(decode('b2c3d4e5f6a1', 'hex'), 1, '2025-10-31', 'ACTIVE'),
(decode('c3d4e5f6a1b2', 'hex'), 2, '2024-07-31', 'BLOCKED'),
(decode('d4e5f6a1b2c3', 'hex'), 3, '2027-01-31', 'EXPIRED');

-- Вставка транзакций
INSERT INTO transactions (producer, consumer, amount, transaction_date) VALUES
(1, 3, 150.00, '2024-12-01 10:15:00'),
(2, 1, 75.50, '2025-01-15 12:00:00'),
(2, 3, 20.00, '2025-03-05 09:30:00'),
(3, 1, 5.00,  '2025-04-01 08:20:00');
