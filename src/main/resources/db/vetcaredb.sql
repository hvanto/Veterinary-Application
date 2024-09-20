-- Run this in MySQL Workbench manually
USE vetcaredb;

DROP TABLE IF EXISTS articles

CREATE TABLE articles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    link VARCHAR(255) NOT NULL,
    description TEXT,
    author VARCHAR(255),
    published_date DATE,
    image_url VARCHAR(255)
);

