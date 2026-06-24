-- ===========================================================================
-- Script para criar manualmente a tabela usada pelo CRUD.
-- Execute este script no seu banco PostgreSQL do Aiven (via psql, DBeaver,
-- pgAdmin, ou o console SQL do próprio Aiven) caso não queira que o
-- programa Java crie a tabela automaticamente (ClienteDAO.criarTabelaSeNaoExistir).
--
-- ALTERE AQUI: nome da tabela e colunas, se necessário, para refletir
-- a estrutura que você quer usar.
-- ===========================================================================

CREATE TABLE IF NOT EXISTS clientes (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150),
    telefone VARCHAR(30)
);

-- Alguns registros de exemplo (opcional, remova se não quiser dados de teste):
-- INSERT INTO clientes (nome, email, telefone) VALUES ('Maria Silva', 'maria@email.com', '27999990000');
-- INSERT INTO clientes (nome, email, telefone) VALUES ('João Souza', 'joao@email.com', '27988880000');
