-- Base de datos: Authors
CREATE DATABASE IF NOT EXISTS Authors;
USE Authors;

CREATE TABLE IF NOT EXISTS literarygenre (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS author (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(150) NOT NULL,
    birth_date DATE         NOT NULL,
    phone      VARCHAR(9)   NOT NULL,
    id_genre   INT,
    CONSTRAINT fk_author_genre
        FOREIGN KEY (id_genre)
        REFERENCES literarygenre(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

-- Datos iniciales
INSERT INTO literarygenre (name) VALUES
('Romance'),
('Novela'),
('Poesía'),
('Ciencia Ficción'),
('Fantasía'),
('Terror'),
('Misterio'),
('Aventura'),
('Biografía'),
('Infantil'),
('Comedia');

INSERT INTO author (name, birth_date, phone, id_genre) VALUES
('Edgar Allan Poe',        '1809-01-19', '2555-1004', 6),
('J.K. Rowling',           '1965-07-31', '3555-1002', 4),
('Gabriel García Márquez', '1927-03-06', '6555-1001', 1),
('Pablo Neruda',           '1904-07-12', '7555-1005', 3),
('Julio Verne',            '1828-02-08', '2555-1008', 8),
('Stephen King',           '1947-09-21', '7555-1010', 6);
