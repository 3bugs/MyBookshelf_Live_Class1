CREATE DATABASE bookshelf;

CREATE TABLE books (
	_id INT PRIMARY KEY AUTO_INCREMENT,
	title VARCHAR(255) NOT NULL,
	subtitle VARCHAR(255),
	isbn VARCHAR(20),
	description TEXT,
	cover_image_filename VARCHAR(255)
);