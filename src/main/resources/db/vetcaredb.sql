-- Run this in MySQL Workbench manually
USE vetcaredb;

CREATE TABLE articles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL
);

INSERT INTO Article (title, content) VALUES 
('How to Care for Your Pet', 'This article explains how to properly care for your pet.'),
('Common Pet Illnesses', 'This article discusses common pet illnesses and how to treat them.'),
('Understanding Pet Behavior', 'Learn about different pet behaviors and what they mean.');
