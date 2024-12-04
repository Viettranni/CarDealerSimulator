DROP DATABASE IF EXISTS car_sim_db;
CREATE DATABASE car_sim_db;
USE car_sim_db;

-- Create the table to store Currency objects
CREATE TABLE bennies_original_motor_works (
                                              id INT AUTO_INCREMENT PRIMARY KEY,
                                              amount INT(10) NOT NULL,
                                              car_type VARCHAR(10) NOT NULL,
                                              fuel_type VARCHAR(10) NOT NULL,
                                              mean_price DECIMAL(10, 4) NOT NULL,
                                              price_variance DECIMAL(10, 4) NOT NULL,
                                              base_price DECIMAL(10, 4) NOT NULL
);

CREATE TABLE legendary_motorsport (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      amount INT(10) NOT NULL,
                                      car_type VARCHAR(10) NOT NULL,
                                      fuel_type VARCHAR(10) NOT NULL,
                                      mean_price DECIMAL(10, 4) NOT NULL,
                                      price_variance DECIMAL(10, 4) NOT NULL,
                                      base_price DECIMAL(10, 4) NOT NULL
);

CREATE TABLE southern_sa_superautos (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        amount INT(10) NOT NULL,
                                        car_type VARCHAR(10) NOT NULL,
                                        fuel_type VARCHAR(10) NOT NULL,
                                        mean_price DECIMAL(10, 4) NOT NULL,
                                        price_variance DECIMAL(10, 4) NOT NULL,
                                        base_price DECIMAL(10, 4) NOT NULL
);

CREATE TABLE premium_deluxe_motorsport (
                                           id INT AUTO_INCREMENT PRIMARY KEY,
                                           amount INT(10) NOT NULL,
                                           car_type VARCHAR(10) NOT NULL,
                                           fuel_type VARCHAR(10) NOT NULL,
                                           mean_price DECIMAL(10, 4) NOT NULL,
                                           price_variance DECIMAL(10, 4) NOT NULL,
                                           base_price DECIMAL(10, 4) NOT NULL
);

CREATE TABLE sunshine_autos (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                amount INT(10) NOT NULL,
                                car_type VARCHAR(10) NOT NULL,
                                fuel_type VARCHAR(10) NOT NULL,
                                mean_price DECIMAL(10, 4) NOT NULL,
                                price_variance DECIMAL(10, 4) NOT NULL,
                                base_price DECIMAL(10, 4) NOT NULL
);

CREATE TABLE luxury_autos (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              amount INT(10) NOT NULL,
                              car_type VARCHAR(10) NOT NULL,
                              fuel_type VARCHAR(10) NOT NULL,
                              mean_price DECIMAL(10, 4) NOT NULL,
                              price_variance DECIMAL(10, 4) NOT NULL,
                              base_price DECIMAL(10, 4) NOT NULL
);

-- Inserting data into bennies_original_motor_works
INSERT INTO bennies_original_motor_works (amount, car_type, fuel_type, mean_price, price_variance, base_price)
VALUES
    (10, 'van', 'gas', 25000.0000, 1500.0000, 23000.0000),
    (8, 'van', 'hybrid', 30000.0000, 1600.0000, 28000.0000),
    (12, 'sport', 'gas', 50000.0000, 2500.0000, 48000.0000),
    (5, 'sport', 'hybrid', 60000.0000, 2700.0000, 57000.0000),
    (15, 'suv', 'electric', 40000.0000, 2000.0000, 38000.0000),
    (10, 'suv', 'gas', 35000.0000, 1800.0000, 33000.0000),
    (7, 'sedan', 'hybrid', 25000.0000, 1300.0000, 24000.0000),
    (14, 'sedan', 'electric', 27000.0000, 1400.0000, 26000.0000),
    (20, 'compact', 'gas', 18000.0000, 1200.0000, 17000.0000),
    (18, 'compact', 'hybrid', 22000.0000, 1300.0000, 21000.0000);

-- Inserting data into legendary_motorsport
INSERT INTO legendary_motorsport (amount, car_type, fuel_type, mean_price, price_variance, base_price)
VALUES
    (5, 'sport', 'gas', 120000.0000, 10000.0000, 110000.0000),
    (4, 'sport', 'hybrid', 130000.0000, 11000.0000, 118000.0000),
    (10, 'suv', 'electric', 80000.0000, 4000.0000, 76000.0000),
    (3, 'suv', 'gas', 90000.0000, 5000.0000, 85000.0000),
    (7, 'sedan', 'hybrid', 55000.0000, 3000.0000, 52000.0000),
    (6, 'sedan', 'electric', 60000.0000, 3500.0000, 57000.0000);

-- Inserting data into southern_sa_superautos
INSERT INTO southern_sa_superautos (amount, car_type, fuel_type, mean_price, price_variance, base_price)
VALUES
    (20, 'van', 'gas', 18000.0000, 1000.0000, 17000.0000),
    (18, 'van', 'electric', 22000.0000, 1200.0000, 21000.0000),
    (25, 'sport', 'gas', 45000.0000, 1800.0000, 43000.0000),
    (15, 'sport', 'electric', 48000.0000, 1900.0000, 46000.0000),
    (30, 'suv', 'gas', 35000.0000, 1500.0000, 33000.0000),
    (22, 'suv', 'hybrid', 38000.0000, 1700.0000, 36000.0000),
    (12, 'sedan', 'gas', 27000.0000, 1300.0000, 26000.0000),
    (14, 'sedan', 'electric', 30000.0000, 1500.0000, 29000.0000),
    (10, 'compact', 'hybrid', 20000.0000, 1100.0000, 19000.0000),
    (16, 'compact', 'gas', 22000.0000, 1200.0000, 21000.0000);

-- Inserting data into premium_deluxe_motorsport
INSERT INTO premium_deluxe_motorsport (amount, car_type, fuel_type, mean_price, price_variance, base_price)
VALUES
    (6, 'sport', 'gas', 90000.0000, 7000.0000, 85000.0000),
    (4, 'sport', 'hybrid', 100000.0000, 8000.0000, 94000.0000),
    (8, 'suv', 'electric', 70000.0000, 4500.0000, 66000.0000),
    (3, 'suv', 'gas', 80000.0000, 6000.0000, 75000.0000),
    (10, 'sedan', 'hybrid', 60000.0000, 4000.0000, 56000.0000),
    (7, 'sedan', 'electric', 65000.0000, 4500.0000, 61000.0000);

-- Inserting data into sunshine_autos
INSERT INTO sunshine_autos (amount, car_type, fuel_type, mean_price, price_variance, base_price)
VALUES
    (12, 'van', 'gas', 23000.0000, 1300.0000, 22000.0000),
    (14, 'van', 'hybrid', 27000.0000, 1400.0000, 26000.0000),
    (16, 'sport', 'gas', 42000.0000, 1600.0000, 40000.0000),
    (10, 'sport', 'electric', 45000.0000, 1700.0000, 43000.0000),
    (18, 'suv', 'hybrid', 37000.0000, 1800.0000, 35000.0000),
    (20, 'suv', 'electric', 39000.0000, 2000.0000, 37000.0000),
    (15, 'sedan', 'gas', 26000.0000, 1200.0000, 25000.0000),
    (13, 'sedan', 'hybrid', 29000.0000, 1300.0000, 28000.0000),
    (17, 'compact', 'electric', 20000.0000, 1100.0000, 19000.0000),
    (22, 'compact', 'gas', 21000.0000, 1150.0000, 20000.0000);

-- Inserting data into luxury_autos
INSERT INTO luxury_autos (amount, car_type, fuel_type, mean_price, price_variance, base_price)
VALUES
    (3, 'van', 'gas', 50000.0000, 3000.0000, 48000.0000),
    (2, 'sport', 'hybrid', 120000.0000, 12000.0000, 110000.0000),
    (5, 'sport', 'electric', 150000.0000, 15000.0000, 140000.0000),
    (4, 'suv', 'gas', 85000.0000, 7000.0000, 80000.0000),
    (6, 'suv', 'electric', 95000.0000, 8000.0000, 90000.0000),
    (7, 'sedan', 'gas', 65000.0000, 5000.0000, 62000.0000),
    (6, 'sedan', 'hybrid', 75000.0000, 6000.0000, 71000.0000);



-- Drop the user account 'appuser' if it exists
DROP USER IF EXISTS 'saed'@'localhost';

-- Create the 'appuser' account with a secure password
CREATE USER 'saed'@'localhost' IDENTIFIED BY '12345678';

-- Grant appropriate privileges to 'appuser'
GRANT SELECT, INSERT, UPDATE, DELETE ON car_sim_db.* TO 'saed'@'localhost';

-- Flush privileges to apply the changes
FLUSH PRIVILEGES;