package br.com.denram.empreendimentos_sc_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MunicipioResponseDto {

    private Long id;
    private String nome;

}