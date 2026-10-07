-- furniture_shop.sql
-- Run this entire script in MySQL Workbench ONCE before starting the application.

CREATE DATABASE IF NOT EXISTS furniture_shop;
USE furniture_shop;

-- ─── Users ────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   CHAR(64)     NOT NULL,          -- SHA-256 hex
    full_name  VARCHAR(100) NOT NULL,
    role       VARCHAR(30)  NOT NULL DEFAULT 'cashier'
);

-- Passwords are SHA-256 hashes:
--   admin123  → 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
--   cashier123 → c5c1f34e2cc15d67b8e06cf0cf9a0e37dd3e498e93cbf78f11cab26acfe3b2e0
INSERT IGNORE INTO users (username, password, full_name, role) VALUES
('admin',   '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Admin User',   'admin'),
('cashier', 'c5c1f34e2cc15d67b8e06cf0cf9a0e37dd3e498e93cbf78f11cab26acfe3b2e0', 'Shop Cashier', 'cashier');

-- ─── Categories ───────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS categories (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(80) NOT NULL UNIQUE
);

INSERT IGNORE INTO categories (name) VALUES
('Bedroom'),
('Living Room'),
('Dining Room'),
('Office'),
('Outdoor'),
('Kitchen'),
('Storage');

-- ─── Customers ────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS customers (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    phone   VARCHAR(15)  NOT NULL UNIQUE,
    email   VARCHAR(120),
    address TEXT
);

INSERT IGNORE INTO customers (name, phone, email, address) VALUES
('Nimal Perera',   '0771234567', 'nimal@example.com',  '12 Main St, Colombo'),
('Kamali Silva',   '0712345678', 'kamali@example.com', '45 Lake Road, Kandy'),
('Roshan Fernando','0751112222', NULL,                  '8 Hill Ave, Galle');

-- ─── Products ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS products (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    category_id   INT            NOT NULL,
    name          VARCHAR(120)   NOT NULL,
    material      VARCHAR(80),
    price         DECIMAL(12,2)  NOT NULL,
    stock_qty     INT            NOT NULL DEFAULT 0,
    reorder_level INT            NOT NULL DEFAULT 5,
    CONSTRAINT fk_product_cat FOREIGN KEY (category_id) REFERENCES categories(id)
);

INSERT IGNORE INTO products (category_id, name, material, price, stock_qty, reorder_level) VALUES
(1, 'Queen Bed Frame',     'Teak',        45000.00, 12, 3),
(1, 'Wardrobe 3-door',     'Mahogany',    68000.00,  4, 5),
(2, 'L-shape Sofa Set',    'Fabric',      85000.00,  6, 2),
(2, 'Coffee Table',        'Glass/Steel', 18500.00, 20, 5),
(3, 'Dining Table 6-seat', 'Teak',        55000.00,  8, 3),
(3, 'Dining Chair',        'Wood',         5500.00, 40, 10),
(4, 'Executive Desk',      'MDF',         32000.00,  3, 5),
(4, 'Ergonomic Chair',     'Mesh',        22000.00,  2, 5);

-- ─── Sales ────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS sales (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    customer_id    INT            NOT NULL,
    user_id        INT            NOT NULL,
    sale_date      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    discount       DECIMAL(12,2)  NOT NULL DEFAULT 0.00,
    total_amount   DECIMAL(12,2)  NOT NULL,
    payment_method VARCHAR(30)    NOT NULL DEFAULT 'CASH',
    CONSTRAINT fk_sale_cust FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_sale_user FOREIGN KEY (user_id)     REFERENCES users(id)
);

-- ─── Sale Items ───────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS sale_items (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    sale_id     INT            NOT NULL,
    product_id  INT            NOT NULL,
    quantity    INT            NOT NULL,
    unit_price  DECIMAL(12,2)  NOT NULL,
    line_total  DECIMAL(12,2)  NOT NULL,
    CONSTRAINT fk_item_sale    FOREIGN KEY (sale_id)    REFERENCES sales(id),
    CONSTRAINT fk_item_product FOREIGN KEY (product_id) REFERENCES products(id)
);
