CREATE TABLE empreendimento (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    nome_responsavel VARCHAR(100) NOT NULL,
    municipio_id BIGINT REFERENCES municipio(id) NOT NULL,
    segmento SMALLINT NOT NULL,
    email VARCHAR(100),
    telefone VARCHAR(50),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMP NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_empreendimento_municipio ON empreendimento(municipio_id);
CREATE INDEX idx_empreendimento_segmento ON empreendimento(segmento);