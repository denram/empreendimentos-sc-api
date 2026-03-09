package br.com.denram.empreendimentos_sc_api.mapper;

import br.com.denram.empreendimentos_sc_api.dto.EmpreendimentoResponseDto;
import br.com.denram.empreendimentos_sc_api.domain.Empreendimento;
import org.springframework.stereotype.Component;

@Component
public class EmpreendimentoMapper {

    public EmpreendimentoResponseDto toResponseDto(Empreendimento entity) {
        return EmpreendimentoResponseDto.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .nomeResponsavel(entity.getNomeResponsavel())
                .municipioId(entity.getMunicipio().getId())
                .municipioNome(entity.getMunicipio().getNome())
                .segmentoIndex(entity.getSegmento().getIndex())
                .segmento(entity.getSegmento().name())
                .email(entity.getEmail())
                .telefone(entity.getTelefone())
                .ativo(entity.getAtivo())
                .criadoEm(entity.getCriadoEm())
                .atualizadoEm(entity.getAtualizadoEm())
                .build();
    }
}