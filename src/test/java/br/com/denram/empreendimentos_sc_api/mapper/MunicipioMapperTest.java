package br.com.denram.empreendimentos_sc_api.mapper;

import br.com.denram.empreendimentos_sc_api.domain.Municipio;
import br.com.denram.empreendimentos_sc_api.dto.MunicipioResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MunicipioMapper")
class MunicipioMapperTest {

    private final MunicipioMapper mapper = new MunicipioMapper();

    @Nested
    @DisplayName("toDto")
    class ToDto {

        @ParameterizedTest
        @NullSource
        @DisplayName("deve retornar null quando entity for null")
        void deveRetornarNullQuandoEntityForNull(Municipio entity) {
            MunicipioResponseDto resultado = mapper.toDto(entity);

            assertNull(resultado);
        }

        @Test
        @DisplayName("deve converter entidade para dto")
        void deveConverterEntidadeParaDto() {
            Municipio entity = novoMunicipio(123L, "Joinville");

            MunicipioResponseDto resultado = mapper.toDto(entity);

            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertEquals(123L, resultado.getId()),
                    () -> assertEquals("Joinville", resultado.getNome())
            );
        }

        @Test
        @DisplayName("deve criar nova instância de dto sem alterar a entidade")
        void deveCriarNovaInstanciaDeDtoSemAlterarEntidade() {
            Municipio entity = novoMunicipio(10L, "Blumenau");

            MunicipioResponseDto resultado = mapper.toDto(entity);

            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertNotSame(entity, resultado),
                    () -> assertEquals(entity.getId(), resultado.getId()),
                    () -> assertEquals(entity.getNome(), resultado.getNome()),
                    () -> assertEquals(10L, entity.getId()),
                    () -> assertEquals("Blumenau", entity.getNome())
            );
        }
    }

    @Nested
    @DisplayName("toDtoPage")
    class ToDtoPage {

        @Test
        @DisplayName("deve converter página de entidades para página de dtos")
        void deveConverterPaginaDeEntidadesParaPaginaDeDtos() {
            PageRequest pageable = PageRequest.of(0, 2);
            Page<Municipio> page = new PageImpl<>(
                    List.of(
                            novoMunicipio(1L, "Joinville"),
                            novoMunicipio(2L, "Florianópolis")
                    ),
                    pageable,
                    2
            );

            Page<MunicipioResponseDto> resultado = mapper.toDtoPage(page);

            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertEquals(2, resultado.getTotalElements()),
                    () -> assertEquals(1, resultado.getTotalPages()),
                    () -> assertEquals(2, resultado.getContent().size()),
                    () -> assertEquals(0, resultado.getNumber()),
                    () -> assertEquals(2, resultado.getSize()),
                    () -> assertEquals(1L, resultado.getContent().get(0).getId()),
                    () -> assertEquals("Joinville", resultado.getContent().get(0).getNome()),
                    () -> assertEquals(2L, resultado.getContent().get(1).getId()),
                    () -> assertEquals("Florianópolis", resultado.getContent().get(1).getNome())
            );
        }

        @Test
        @DisplayName("deve retornar página vazia quando página de origem estiver vazia")
        void deveRetornarPaginaVaziaQuandoPaginaOrigemEstiverVazia() {
            PageRequest pageable = PageRequest.of(0, 10);
            Page<Municipio> page = new PageImpl<>(List.of(), pageable, 0);

            Page<MunicipioResponseDto> resultado = mapper.toDtoPage(page);

            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertTrue(resultado.getContent().isEmpty()),
                    () -> assertEquals(0, resultado.getTotalElements()),
                    () -> assertEquals(0, resultado.getNumber()),
                    () -> assertEquals(10, resultado.getSize())
            );
        }
    }

    private static Municipio novoMunicipio(Long id, String nome) {
        Municipio municipio = new Municipio(id, nome);
        return municipio;
    }
}