CREATE DATABASE IF NOT EXISTS Authors;
USE Authors;

CREATE TABLE IF NOT EXISTS literarygenre (
    id_genre INT AUTO_INCREMENT PRIMARY KEY,
    name_genre VARCHAR(100) NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS author (
    id_author INT AUTO_INCREMENT PRIMARY KEY,
    name_author VARCHAR(150) NOT NULL,
    birth_date DATE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    id_genre INT,
    CONSTRAINT fk_author_genre 
        FOREIGN KEY (id_genre) 
        REFERENCES literarygenre(id_genre)
        ON DELETE SET NULL 
        ON UPDATE CASCADE
);


/* INSERTS PARA DATOS BASICOS */
INSERT INTO literarygenre (name_genre, description) VALUES
('Romance', 'Historias sobre relaciones amorosas'),
('Novela', 'Narración de hechos ficticios o reales'),
('Poesía', 'Expresión artística mediante versos y ritmo'),
('Ciencia Ficción', 'Relatos basados en avances científicos y tecnológicos'),
('Fantasía', 'Historias con elementos mágicos y mundos imaginarios'),
('Terror', 'Relatos diseñados para causar miedo o suspenso'),
('Misterio', 'Narraciones con enigmas o crímenes por resolver'),
('Aventura', 'Historias llenas de acción y exploración'),
('Biografía', 'Relato de la vida de una persona'),
('Infantil', 'Literatura dirigida a niños'),
('Comedia', 'Obras con tono humorístico y entretenido');

INSERT INTO author (name_author, birth_date, phone, id_genre) VALUES
('Edgar Allan Poe', '1809-01-19', '555-1004', 6),
('J.K. Rowling', '1965-07-31', '555-1002', 4),
('Gabriel García Márquez', '1927-03-06', '555-1001', 1),
('Pablo Neruda', '1904-07-12', '555-1005', 2),
('Julio Verne', '1828-02-08', '555-1008', 9),
('Stephen King', '1947-09-21', '555-1010', 6);