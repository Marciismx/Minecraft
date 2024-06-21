-- Dit script creëert de benodigde database en tabel voor de TarkovEscape plugin

-- Vervang 'tarkov_escape_db' door de naam van je gewenste database
CREATE DATABASE IF NOT EXISTS tarkov_escape_db;

-- Gebruik de nieuw aangemaakte database
USE tarkov_escape_db;

-- Creëer de tabel voor spelergegevens
CREATE TABLE IF NOT EXISTS player_data (
    uuid VARCHAR(36) PRIMARY KEY,
    data TEXT NOT NULL
);

CREATE TABLE loottable (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rarity VARCHAR(50),
    item_name VARCHAR(100),
    item_material VARCHAR(50),
    drop_chance DOUBLE
);

CREATE TABLE lootrolls (
    id INT AUTO_INCREMENT PRIMARY KEY,
    roll_number INT,
    min_items INT,
    max_items INT
);

-- Geef een succesbericht
SELECT 'Database en tabel zijn succesvol aangemaakt' AS 'Status';