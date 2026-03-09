package br.com.denram.empreendimentos_sc_api.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EmpreendimentoResponseDto {

    Long id;
    String nome;
    String nomeResponsavel;
    Long municipioId;
    String municipioNome;
    Short segmentoIndex;
    String segmento;
    String email;
    String telefone;
    Boolean ativo;
    LocalDateTime criadoEm;
    LocalDateTime atualizadoEm;
}