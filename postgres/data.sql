-- Вставка пользователей
INSERT INTO users (first_name, last_name, email, password, role, created_at, updated_at, enabled) VALUES
('Alice', 'Smith', 'alice@example.com', 'hashed_pw_1', 'USER', now(), now(), true),
('Bob', 'Johnson', 'bob@example.com', 'hashed_pw_2', 'USER', now(), now(), true),
('Charlie', 'Admin', 'admin@example.com', 'hashed_pw_admin', 'ADMIN', now(), now(), true);

-- Вставка карт (зашифрованный номер и его хэш симулируются через HEX)
INSERT INTO cards (card_number, card_number_hash, card_owner, validity_period, status) VALUES
(decode('a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6', 'hex'), decode('aa11bb22cc33dd44ee55ff66aa77bb88', 'hex'), 1, '2026-12-31', 'ACTIVE'),
(decode('b2c3d4e5f6a1b8c9d0e1f2a3b4c5d6a7', 'hex'), decode('bb22cc33dd44ee55ff66aa77bb88aa11', 'hex'), 1, '2025-10-31', 'ACTIVE'),
(decode('c3d4e5f6a1b2b8c9d0e1f2a3b4c5d6a7', 'hex'), decode('cc33dd44ee55ff66aa77bb88aa11bb22', 'hex'), 2, '2024-07-31', 'BLOCKED'),
(decode('d4e5f6a1b2c3c9d0e1f2a3b4c5d6a7b8', 'hex'), decode('dd44ee55ff66aa77bb88aa11bb22cc33', 'hex'), 3, '2027-01-31', 'EXPIRED');

-- Вставка транзакций
INSERT INTO transactions (producer, consumer, amount, transaction_date) VALUES
(1, 3, 150.00, '2024-12-01 10:15:00'),
(2, 1, 75.50, '2025-01-15 12:00:00'),
(2, 3, 20.00, '2025-03-05 09:30:00'),
(3, 1, 5.00,  '2025-04-01 08:20:00');
