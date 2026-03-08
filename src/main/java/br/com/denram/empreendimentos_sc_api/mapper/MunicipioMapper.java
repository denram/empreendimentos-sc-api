package br.com.denram.empreendimentos_sc_api.mapper;

import br.com.denram.empreendimentos_sc_api.domain.Municipio;
import br.com.denram.empreendimentos_sc_api.dto.MunicipioResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class MunicipioMapper {

    public MunicipioResponseDto toDto(Municipio entity) {
        if (entity == null) {
            return null;
        }

        return MunicipioResponseDto.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .build();
    }

    public Page<MunicipioResponseDto> toDtoPage(Page<Municipio> page) {
        return page.map(this::toDto);
    }
}