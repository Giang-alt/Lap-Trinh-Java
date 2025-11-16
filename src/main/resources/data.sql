-- This script is intended to be run automatically by Spring Boot on startup.
USE ev_marketplace;
-- It seeds the database with initial data for the year 2025.

-- Users (Password for all is: password)
INSERT INTO users (id, username, email, password, role, status, created_date) VALUES
(1, 'admin', 'admin@evmarketplace.com', '$2a$12$GT57U4Z8pb1JTKDk1edcv.lqKFmVsM5iGdUWPJpKsxh6tM9let9lm', 'ADMIN', 'ACTIVE', '2025-01-01 00:00:00'),
(2, 'consumer', 'consumer@evmarketplace.com', '$2a$12$GT57U4Z8pb1JTKDk1edcv.lqKFmVsM5iGdUWPJpKsxh6tM9let9lm', 'DATA_CONSUMER', 'ACTIVE', '2025-01-01 00:00:00'),
(3, 'provider', 'provider@evmarketplace.com', '$2a$12$GT57U4Z8pb1JTKDk1edcv.lqKFmVsM5iGdUWPJpKsxh6tM9let9lm', 'DATA_PROVIDER', 'ACTIVE', '2025-01-01 00:00:00');

-- Data Consumers
INSERT INTO data_consumers (user_id, company_name, industry, subscription_type) VALUES
(2, 'EV Research Lab 2025', 'Research', 'PREMIUM');

-- Data Providers
INSERT INTO data_providers (user_id, company_name, data_source_type, verification_status) VALUES
(3, 'Future Mobility Data Corp', 'OEM', 'VERIFIED');

-- Admins
INSERT INTO admins (user_id, permissions, access_level) VALUES
(1, 'ALL', 'SUPER_ADMIN');

-- Data Sources
INSERT INTO data_sources (id, name, type, description, data_provider_id, status, created_date) VALUES
(1, 'Future Mobility Battery Data 2025', 'BATTERY_DATA', 'Comprehensive battery performance data from 2025 vehicle models', 3, 'ACTIVE', '2025-02-01 09:00:00'),
(2, 'National Charging Network Usage 2025', 'CHARGING_DATA', 'Real-time usage patterns from charging stations for Q1 2025', 3, 'ACTIVE', '2025-02-05 14:00:00');

-- Data Packages
INSERT INTO data_packages (id, name, description, data_type, format, file_size, price, pricing_model, data_source_id, status) VALUES
(1, 'NextGen EV Battery SoC Data 2025', 'State of Charge data for 2025 EV models over 6 months', 'RAW_DATA', 'CSV', 10485760, 5500.00, 'ONE_TIME', 1, 'ACTIVE'),
(2, 'Q1 2025 Charging Patterns Analysis', 'Analysis of EV charging patterns and peak usage times for the first quarter of 2025', 'ANALYTICS_REPORT', 'PDF', 5242880, 3500.00, 'ONE_TIME', 2, 'ACTIVE'),
(3, '2025 Battery Health Dashboard', 'Interactive dashboard showing battery health metrics for the 2025 fleet', 'DASHBOARD', 'API', 0, 12000.00, 'SUBSCRIPTION', 1, 'PENDING_APPROVAL');
