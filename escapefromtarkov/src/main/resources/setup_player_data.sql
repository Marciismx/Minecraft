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

-- Geef een succesbericht
SELECT 'Database en tabel zijn succesvol aangemaakt' AS 'Status';