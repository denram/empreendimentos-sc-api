package br.com.denram.empreendimentos_sc_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmpreendimentoRequestDto {

    @NotBlank(message = "O campo nome é obrigatório.")
    @Size(max = 100, message = "O campo nome deve ter no máximo 100 caracteres.")
    private String nome;

    @NotBlank(message = "O campo nomeResponsavel é obrigatório.")
    @Size(max = 100, message = "O campo nomeResponsavel deve ter no máximo 100 caracteres.")
    private String nomeResponsavel;

    @NotNull(message = "O campo municipioId é obrigatório.")
    private Long municipioId;

    @NotBlank(message = "O campo segmento é obrigatório.")
    private String segmento;

    @Email(message = "O campo email deve possuir um endereço válido.")
    @Size(max = 100, message = "O campo email deve ter no máximo 100 caracteres.")
    private String email;

    @Size(max = 50, message = "O campo telefone deve ter no máximo 50 caracteres.")
    private String telefone;

    private Boolean ativo;
}