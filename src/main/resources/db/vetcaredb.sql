-- Run this in MySQL Workbench manually
CREATE DATABASE IF NOT EXISTS vetcaredb;
USE vetcaredb;

CREATE TABLE articles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL
);


CREATE TABLE DatabaseInfo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    connectionStatus VARCHAR(255) NOT NULL
);

CREATE TABLE `User` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Sample data (optional)

INSERT INTO articles (title, content) VALUES
('How to Care for Your Pet', 'This article explains how to properly care for your pet.'),
('Common Pet Illnesses', 'This article discusses common pet illnesses and how to treat them.'),
('Understanding Pet Behavior', 'Learn about different pet behaviors and what they mean.');

INSERT INTO DatabaseInfo (connectionStatus) VALUES ('Connected');
INSERT INTO User (name, email, phone, password) VALUES
('John Doe', 'john@example.com', '123-456-7890', 'password123');
