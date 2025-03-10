CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE IF NOT EXISTS bank_account (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    balance DECIMAL(19, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS category (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(10) NOT NULL CHECK (type IN ('INCOME', 'EXPENSE'))
);

CREATE TABLE IF NOT EXISTS operation (
    id UUID PRIMARY KEY,
    type VARCHAR(10) NOT NULL CHECK (type IN ('INCOME', 'EXPENSE')),
    bank_account_id UUID NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    date TIMESTAMP NOT NULL,
    description VARCHAR(500),
    category_id UUID NOT NULL,
    FOREIGN KEY (bank_account_id) REFERENCES bank_account(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE INDEX IF NOT EXISTS idx_operation_bank_account ON operation(bank_account_id);
CREATE INDEX IF NOT EXISTS idx_operation_category ON operation(category_id);
CREATE INDEX IF NOT EXISTS idx_operation_date ON operation(date);
