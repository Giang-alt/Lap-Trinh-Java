-- EV Data Analytics Marketplace Database Schema
-- MySQL 8.0+

CREATE DATABASE IF NOT EXISTS ev_marketplace;
USE ev_marketplace;

-- Users table (base table for all user types)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('DATA_CONSUMER', 'DATA_PROVIDER', 'ADMIN') NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') DEFAULT 'ACTIVE'
);

-- Data Consumers table
CREATE TABLE data_consumers (
    user_id BIGINT PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL,
    industry VARCHAR(100),
    subscription_type ENUM('BASIC', 'PREMIUM', 'ENTERPRISE') DEFAULT 'BASIC',
    preferences TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Data Providers table
CREATE TABLE data_providers (
    user_id BIGINT PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL,
    data_source_type ENUM('OEM', 'CHARGING_STATION', 'FLEET_OPERATOR', 'RESEARCH_INSTITUTE', 'OTHER') NOT NULL,
    verification_status ENUM('PENDING', 'VERIFIED', 'REJECTED') DEFAULT 'PENDING',
    revenue DECIMAL(10,2) DEFAULT 0.00,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Admins table
CREATE TABLE admins (
    user_id BIGINT PRIMARY KEY,
    permissions VARCHAR(100) NOT NULL,
    access_level ENUM('SUPER_ADMIN', 'ADMIN', 'MODERATOR') DEFAULT 'SUPER_ADMIN',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Data Sources table
CREATE TABLE data_sources (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type ENUM('BATTERY_DATA', 'CHARGING_DATA', 'DRIVING_BEHAVIOR', 'V2G_TRANSACTIONS', 'FLEET_DATA', 'RESEARCH_DATA') NOT NULL,
    description VARCHAR(500),
    data_provider_id BIGINT NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') DEFAULT 'ACTIVE',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (data_provider_id) REFERENCES data_providers(user_id) ON DELETE CASCADE
);

-- Data Packages table
CREATE TABLE data_packages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    data_type ENUM('RAW_DATA', 'PROCESSED_DATA', 'ANALYTICS_REPORT', 'DASHBOARD') NOT NULL,
    format ENUM('CSV', 'JSON', 'XML', 'EXCEL', 'PDF', 'API') NOT NULL,
    file_size BIGINT,
    price DECIMAL(10,2) NOT NULL,
    pricing_model ENUM('ONE_TIME', 'SUBSCRIPTION', 'PER_DOWNLOAD') DEFAULT 'ONE_TIME',
    data_source_id BIGINT NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED', 'PENDING_APPROVAL') DEFAULT 'PENDING_APPROVAL',
    file_path VARCHAR(500),
    FOREIGN KEY (data_source_id) REFERENCES data_sources(id) ON DELETE CASCADE
);

-- Data Categories table (for future use)
CREATE TABLE data_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    parent_category_id BIGINT,
    FOREIGN KEY (parent_category_id) REFERENCES data_categories(id) ON DELETE SET NULL
);

-- Data Filters table (for future use)
CREATE TABLE data_filters (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    filter_type VARCHAR(50) NOT NULL,
    filter_value VARCHAR(100) NOT NULL,
    data_package_id BIGINT NOT NULL,
    FOREIGN KEY (data_package_id) REFERENCES data_packages(id) ON DELETE CASCADE
);

-- Data Purchases table (for future use)
CREATE TABLE data_purchases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_consumer_id BIGINT NOT NULL,
    data_package_id BIGINT NOT NULL,
    purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(10,2) NOT NULL,
    status ENUM('PENDING', 'COMPLETED', 'CANCELLED', 'REFUNDED') DEFAULT 'PENDING',
    payment_method VARCHAR(50),
    FOREIGN KEY (data_consumer_id) REFERENCES data_consumers(user_id) ON DELETE CASCADE,
    FOREIGN KEY (data_package_id) REFERENCES data_packages(id) ON DELETE CASCADE
);

-- Payments table (for future use)
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_purchase_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    transaction_id VARCHAR(100) UNIQUE,
    status ENUM('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED') DEFAULT 'PENDING',
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (data_purchase_id) REFERENCES data_purchases(id) ON DELETE CASCADE
);

-- Data Access table (for future use)
CREATE TABLE data_access (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_consumer_id BIGINT NOT NULL,
    data_package_id BIGINT NOT NULL,
    access_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    access_type ENUM('VIEW', 'DOWNLOAD', 'API') NOT NULL,
    ip_address VARCHAR(45),
    FOREIGN KEY (data_consumer_id) REFERENCES data_consumers(user_id) ON DELETE CASCADE,
    FOREIGN KEY (data_package_id) REFERENCES data_packages(id) ON DELETE CASCADE
);

-- API Keys table (for future use)
CREATE TABLE api_keys (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_consumer_id BIGINT NOT NULL,
    key_value VARCHAR(255) NOT NULL UNIQUE,
    permissions TEXT,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_date TIMESTAMP,
    status ENUM('ACTIVE', 'INACTIVE', 'EXPIRED') DEFAULT 'ACTIVE',
    FOREIGN KEY (data_consumer_id) REFERENCES data_consumers(user_id) ON DELETE CASCADE
);

-- Revenue table (for future use)
CREATE TABLE revenue (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_provider_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    commission DECIMAL(10,2) NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_package_id BIGINT NOT NULL,
    FOREIGN KEY (data_provider_id) REFERENCES data_providers(user_id) ON DELETE CASCADE,
    FOREIGN KEY (data_package_id) REFERENCES data_packages(id) ON DELETE CASCADE
);

-- Insert sample data
INSERT INTO users (username, email, password, role, status) VALUES
('admin', 'admin@evmarketplace.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'ADMIN', 'ACTIVE'),
('consumer1', 'consumer1@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'DATA_CONSUMER', 'ACTIVE'),
('provider1', 'provider1@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'DATA_PROVIDER', 'ACTIVE');

INSERT INTO data_consumers (user_id, company_name, industry, subscription_type) VALUES
(2, 'EV Research Lab', 'Research', 'PREMIUM');

INSERT INTO data_providers (user_id, company_name, data_source_type, verification_status) VALUES
(3, 'Tesla Data Corp', 'OEM', 'VERIFIED');

INSERT INTO admins (user_id, permissions, access_level) VALUES
(1, 'ALL', 'SUPER_ADMIN');

INSERT INTO data_sources (name, type, description, data_provider_id, status) VALUES
('Tesla Battery Performance Data', 'BATTERY_DATA', 'Comprehensive battery performance data from Tesla vehicles', 3, 'ACTIVE'),
('EV Charging Station Usage', 'CHARGING_DATA', 'Real-time charging station usage patterns and statistics', 3, 'ACTIVE');

INSERT INTO data_packages (name, description, data_type, format, file_size, price, pricing_model, data_source_id, status) VALUES
('Tesla Model 3 Battery SoC Data', 'State of Charge data for Tesla Model 3 vehicles over 6 months', 'RAW_DATA', 'CSV', 10485760, 500000.00, 'ONE_TIME', 1, 'ACTIVE'),
('EV Charging Patterns Analysis', 'Analysis of EV charging patterns and peak usage times', 'ANALYTICS_REPORT', 'PDF', 5242880, 300000.00, 'ONE_TIME', 2, 'ACTIVE'),
('Battery Health Dashboard', 'Interactive dashboard showing battery health metrics', 'DASHBOARD', 'API', 0, 1000000.00, 'SUBSCRIPTION', 1, 'PENDING_APPROVAL');

-- Create indexes for better performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_data_packages_status ON data_packages(status);
CREATE INDEX idx_data_packages_data_type ON data_packages(data_type);
CREATE INDEX idx_data_packages_price ON data_packages(price);
CREATE INDEX idx_data_sources_provider ON data_sources(data_provider_id);
