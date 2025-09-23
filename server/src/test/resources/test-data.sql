-- Очистка таблиц (в правильном порядке из-за foreign keys)
DELETE FROM comments;
DELETE FROM bookings;
DELETE FROM items;
DELETE FROM requests;
DELETE FROM users;

-- Сброс идентификаторов для H2
ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
ALTER TABLE requests ALTER COLUMN id RESTART WITH 1;
ALTER TABLE items ALTER COLUMN id RESTART WITH 1;
ALTER TABLE bookings ALTER COLUMN id RESTART WITH 1;
ALTER TABLE comments ALTER COLUMN id RESTART WITH 1;

-- Пользователи (не указываем ID - пусть генерируются автоматически)
INSERT INTO users (name, email) VALUES
('Owner User', 'owner@example.com'),
('Booker User', 'booker@example.com'),
('Other User', 'other@example.com');

-- Запросы (не указываем ID - пусть генерируются автоматически)
INSERT INTO requests (description, requestor_id, created) VALUES
('Test request for items', 2, '2023-01-03T10:00:00');

-- Вещи (не указываем ID - пусть генерируются автоматически)
INSERT INTO items (name, description, available, owner_id, request_id) VALUES
('Laptop', 'High-performance laptop', true, 1, null),
('Camera', 'Professional camera', false, 1, null),
('Phone', 'Smartphone', true, 1, null),
('Book', 'Interesting book', true, 2, null),
('Tablet', 'Portable tablet', true, 2, null),
('Item with Request', 'Item created from request', true, 1, 1);  -- request_id = 1 (первый созданный запрос)

-- Бронирования (прошедшие даты для завершенных бронирований, не указываем ID)
INSERT INTO bookings (start_date, end_date, item_id, booker_id, status) VALUES
('2023-01-10 10:00:00', '2023-01-12 12:00:00', 1, 2, 'APPROVED'),
('2023-01-18 10:00:00', '2023-01-20 12:00:00', 4, 1, 'APPROVED'),
('2024-12-01 10:00:00', '2024-12-05 12:00:00', 3, 2, 'WAITING'),
('2023-02-10 10:00:00', '2023-02-15 12:00:00', 1, 3, 'REJECTED');

-- Комментарии (не указываем ID)
INSERT INTO comments (text, item_id, author_id, created) VALUES
('Great item! Very useful.', 1, 2, '2023-01-15 10:00:00'),
('Good quality, would recommend', 1, 3, '2023-01-16 14:30:00'),
('Nice item for the price', 4, 1, '2023-01-17 09:15:00');