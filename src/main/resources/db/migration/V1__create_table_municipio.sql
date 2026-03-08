CREATE TABLE municipio (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);
ALTER TABLE municipio ADD CONSTRAINT uk_municipio_nome UNIQUE (nome);
CREATE INDEX idx_municipio_nome ON municipio (nome);