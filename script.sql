-- Criação do banco e tabela, execute o script.sql no MySQL
-- para criar o banco e a tabela caso necessario.
CREATE DATABASE IF NOT EXISTS fintrack;
USE fintrack;

CREATE TABLE transacoes (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            descricao VARCHAR(100) NOT NULL,
                            valor DECIMAL(10,2) NOT NULL,
                            tipo VARCHAR(10) NOT NULL,
                            data DATE NOT NULL
);