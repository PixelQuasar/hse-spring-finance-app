-- Bank accounts (passwords: password1, password2, password3)
INSERT INTO bank_account (id, name, balance, password_hash, phone_number, card_number)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Main Account', 5000.00,
        '$2a$10$WRPDlFPKk4HsYqGP8gN1ZO1Z5bN7vcIY.dSrR.Z7dz5kW9UvEAzCe', '+12025550178', '1234567890123456'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'Savings Account', 10000.00,
        '$2a$10$ksSgzgGcNC6T3bHNLEP4wuBUXHXNSZ0ZbjiQHoNABrUcLnU0SdkaG', '+12025550143', '9876543210987654'),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a33', 'Investment Account', 15000.00,
        '$2a$10$UxaR8DpzDXb3VTwmF7s3WeQoUjlX6LL3ZrAsiBeTbzoauJjwkVx4.', '+12025550191', '5555666677778888');

INSERT INTO category (id, name, type)
VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a44', 'Salary', 'INCOME'),
       ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a55', 'Cashback', 'INCOME'),
       ('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a66', 'Gifts', 'INCOME'),
       ('a1eebc99-9c0b-4ef8-bb6d-6bb9bd380a77', 'Investments', 'INCOME');

INSERT INTO category (id, name, type)
VALUES ('b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a88', 'Groceries', 'EXPENSE'),
       ('c1eebc99-9c0b-4ef8-bb6d-6bb9bd380a99', 'Dining', 'EXPENSE'),
       ('d1eebc99-9c0b-4ef8-bb6d-6bb9bd380aaa', 'Healthcare', 'EXPENSE'),
       ('e1eebc99-9c0b-4ef8-bb6d-6bb9bd380abb', 'Transportation', 'EXPENSE'),
       ('f1eebc99-9c0b-4ef8-bb6d-6bb9bd380acc', 'Entertainment', 'EXPENSE'),
       ('a2eebc99-9c0b-4ef8-bb6d-6bb9bd380add', 'Utilities', 'EXPENSE');

INSERT INTO operation (id, type, bank_account_id, amount, date, description, category_id)
VALUES ('b2eebc99-9c0b-4ef8-bb6d-6bb9bd380aee', 'INCOME', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        3500.00, '2023-09-10 10:00:00', 'August Salary', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a44'),
       ('c2eebc99-9c0b-4ef8-bb6d-6bb9bd380aff', 'INCOME', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        120.00, '2023-09-15 15:30:00', 'Credit Card Cashback', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a55'),
       ('d2eebc99-9c0b-4ef8-bb6d-6bb9bd380b00', 'INCOME', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        3500.00, '2023-10-10 10:00:00', 'September Salary', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a44'),

       ('e2eebc99-9c0b-4ef8-bb6d-6bb9bd380b11', 'EXPENSE', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        175.50, '2023-09-12 18:45:00', 'Walmart Shopping', 'b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a88'),
       ('f2eebc99-9c0b-4ef8-bb6d-6bb9bd380b22', 'EXPENSE', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        85.20, '2023-09-18 20:30:00', 'Dinner at Italian Restaurant', 'c1eebc99-9c0b-4ef8-bb6d-6bb9bd380a99'),
       ('a3eebc99-9c0b-4ef8-bb6d-6bb9bd380b33', 'EXPENSE', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        250.00, '2023-09-25 14:20:00', 'Dentist Appointment', 'd1eebc99-9c0b-4ef8-bb6d-6bb9bd380aaa'),
       ('b3eebc99-9c0b-4ef8-bb6d-6bb9bd380b44', 'EXPENSE', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        175.30, '2023-10-05 12:15:00', 'Electricity and Water Bills', 'a2eebc99-9c0b-4ef8-bb6d-6bb9bd380add');

INSERT INTO operation (id, type, bank_account_id, amount, date, description, category_id)
VALUES ('c3eebc99-9c0b-4ef8-bb6d-6bb9bd380b55', 'INCOME', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22',
        500.00, '2023-09-20 09:30:00', 'Monthly Savings Transfer', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a44'),
       ('d3eebc99-9c0b-4ef8-bb6d-6bb9bd380b66', 'INCOME', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22',
        500.00, '2023-10-20 09:30:00', 'Monthly Savings Transfer', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a44'),

       ('e3eebc99-9c0b-4ef8-bb6d-6bb9bd380b77', 'EXPENSE', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22',
        300.00, '2023-10-15 16:45:00', 'Withdrawal for Weekend Trip', 'f1eebc99-9c0b-4ef8-bb6d-6bb9bd380acc');

INSERT INTO operation (id, type, bank_account_id, amount, date, description, category_id)
VALUES ('f3eebc99-9c0b-4ef8-bb6d-6bb9bd380b88', 'INCOME', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a33',
        125.50, '2023-09-25 14:00:00', 'Quarterly Dividends', 'a1eebc99-9c0b-4ef8-bb6d-6bb9bd380a77'),
       ('a4eebc99-9c0b-4ef8-bb6d-6bb9bd380b99', 'INCOME', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a33',
        820.00, '2023-10-01 10:00:00', 'Stock Sale Profit', 'a1eebc99-9c0b-4ef8-bb6d-6bb9bd380a77'),

       ('b4eebc99-9c0b-4ef8-bb6d-6bb9bd380baa', 'EXPENSE', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a33',
        450.00, '2023-10-12 11:30:00', 'Stock Purchase', 'a1eebc99-9c0b-4ef8-bb6d-6bb9bd380a77'),
       ('c4eebc99-9c0b-4ef8-bb6d-6bb9bd380bbb', 'EXPENSE', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a33',
        25.00, '2023-10-18 15:20:00', 'Broker Fee', 'a1eebc99-9c0b-4ef8-bb6d-6bb9bd380a77');

INSERT INTO operation (id, type, bank_account_id, amount, date, description, category_id)
VALUES ('d4eebc99-9c0b-4ef8-bb6d-6bb9bd380bcc', 'INCOME', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        3500.00, '2023-11-10 10:00:00', 'October Salary', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a44'),
       ('e4eebc99-9c0b-4ef8-bb6d-6bb9bd380bdd', 'EXPENSE', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        210.75, '2023-11-12 18:30:00', 'Weekly Grocery Shopping', 'b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a88'),
       ('f4eebc99-9c0b-4ef8-bb6d-6bb9bd380bee', 'EXPENSE', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        62.50, '2023-11-15 19:45:00', 'Movie and Dinner', 'f1eebc99-9c0b-4ef8-bb6d-6bb9bd380acc'),

       ('a5eebc99-9c0b-4ef8-bb6d-6bb9bd380bff', 'INCOME', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22',
        500.00, '2023-11-05 10:30:00', 'Monthly Savings Transfer', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a44'),

       ('b5eebc99-9c0b-4ef8-bb6d-6bb9bd380c11', 'INCOME', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a33',
        140.25, '2023-11-08 14:20:00', 'Dividend Payment', 'a1eebc99-9c0b-4ef8-bb6d-6bb9bd380a77');
