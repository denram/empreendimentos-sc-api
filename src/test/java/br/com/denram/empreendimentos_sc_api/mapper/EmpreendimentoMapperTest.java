package br.com.denram.empreendimentos_sc_api.mapper;


import br.com.denram.empreendimentos_sc_api.domain.Empreendimento;
import br.com.denram.empreendimentos_sc_api.domain.Municipio;
import br.com.denram.empreendimentos_sc_api.domain.Segmento;
import br.com.denram.empreendimentos_sc_api.dto.EmpreendimentoResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("EmpreendimentoMapper")
class EmpreendimentoMapperTest {

    private final EmpreendimentoMapper mapper = new EmpreendimentoMapper();

    @Nested
    @DisplayName("toResponseDto")
    class ToResponseDto {

        @Test
        @DisplayName("deve converter entidade para dto")
        void deveConverterEntidadeParaDto() {
            LocalDateTime criadoEm = LocalDateTime.of(2026, 3, 9, 10, 30, 0);
            LocalDateTime atualizadoEm = LocalDateTime.of(2026, 3, 9, 11, 45, 0);

            Municipio municipio = new Municipio(42L, "Joinville");

            Segmento segmento = primeiroSegmentoDisponivel();

            Empreendimento entity = new Empreendimento();
            entity.setId(100L);
            entity.setNome("Empreendimento Alpha");
            entity.setNomeResponsavel("Denis Ramos");
            entity.setMunicipio(municipio);
            entity.setSegmento(segmento);
            entity.setEmail("alpha@empresa.com");
            entity.setTelefone("47999990000");
            entity.setAtivo(true);
            entity.setCriadoEm(criadoEm);
            entity.setAtualizadoEm(atualizadoEm);

            EmpreendimentoResponseDto resultado = mapper.toResponseDto(entity);

            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertEquals(100L, resultado.getId()),
                    () -> assertEquals("Empreendimento Alpha", resultado.getNome()),
                    () -> assertEquals("Denis Ramos", resultado.getNomeResponsavel()),
                    () -> assertEquals(42L, resultado.getMunicipioId()),
                    () -> assertEquals("Joinville", resultado.getMunicipioNome()),
                    () -> assertEquals(segmento.getIndex(), resultado.getSegmentoIndex()),
                    () -> assertEquals(segmento.name(), resultado.getSegmento()),
                    () -> assertEquals("alpha@empresa.com", resultado.getEmail()),
                    () -> assertEquals("47999990000", resultado.getTelefone()),
                    () -> assertTrue(resultado.getAtivo()),
                    () -> assertEquals(criadoEm, resultado.getCriadoEm()),
                    () -> assertEquals(atualizadoEm, resultado.getAtualizadoEm())
            );
        }

        @Test
        @DisplayName("deve criar nova instância de dto sem alterar a entidade")
        void deveCriarNovaInstanciaDeDtoSemAlterarEntidade() {
            Municipio municipio = new Municipio(7L, "Blumenau");

            Segmento segmento = primeiroSegmentoDisponivel();

            Empreendimento entity = new Empreendimento();
            entity.setId(1L);
            entity.setNome("Empreendimento Beta");
            entity.setNomeResponsavel("Responsável Beta");
            entity.setMunicipio(municipio);
            entity.setSegmento(segmento);
            entity.setEmail("beta@empresa.com");
            entity.setTelefone("47888887777");
            entity.setAtivo(false);

            EmpreendimentoResponseDto resultado = mapper.toResponseDto(entity);

            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertNotSame(entity, resultado),
                    () -> assertEquals(entity.getId(), resultado.getId()),
                    () -> assertEquals(entity.getNome(), resultado.getNome()),
                    () -> assertEquals(entity.getNomeResponsavel(), resultado.getNomeResponsavel()),
                    () -> assertEquals(entity.getMunicipio().getId(), resultado.getMunicipioId()),
                    () -> assertEquals(entity.getMunicipio().getNome(), resultado.getMunicipioNome()),
                    () -> assertEquals(entity.getSegmento().getIndex(), resultado.getSegmentoIndex()),
                    () -> assertEquals(entity.getSegmento().name(), resultado.getSegmento()),
                    () -> assertEquals(entity.getEmail(), resultado.getEmail()),
                    () -> assertEquals(entity.getTelefone(), resultado.getTelefone()),
                    () -> assertEquals(entity.getAtivo(), resultado.getAtivo())
            );
        }

        @Test
        @DisplayName("deve lançar exceção quando entidade for null")
        void deveLancarExcecaoQuandoEntidadeForNull() {
            assertThrows(NullPointerException.class, () -> mapper.toResponseDto(null));
        }

        @Test
        @DisplayName("deve lançar exceção quando município for null")
        void deveLancarExcecaoQuandoMunicipioForNull() {
            Empreendimento entity = new Empreendimento();
            entity.setId(1L);
            entity.setNome("Empreendimento Sem Município");
            entity.setSegmento(primeiroSegmentoDisponivel());

            assertThrows(NullPointerException.class, () -> mapper.toResponseDto(entity));
        }

        @Test
        @DisplayName("deve lançar exceção quando segmento for null")
        void deveLancarExcecaoQuandoSegmentoForNull() {
            Municipio municipio = new Municipio(1L, "Florianópolis");

            Empreendimento entity = new Empreendimento();
            entity.setId(2L);
            entity.setNome("Empreendimento Sem Segmento");
            entity.setMunicipio(municipio);

            assertThrows(NullPointerException.class, () -> mapper.toResponseDto(entity));
        }
    }

    private static Segmento primeiroSegmentoDisponivel() {
        Segmento[] valores = Segmento.values();
        if (valores.length == 0) {
            throw new IllegalStateException("O enum Segmento precisa ter ao menos um valor para os testes.");
        }
        return valores[0];
    }
}