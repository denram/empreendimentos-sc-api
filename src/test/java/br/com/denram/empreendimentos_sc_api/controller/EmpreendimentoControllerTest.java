package br.com.denram.empreendimentos_sc_api.controller;

import br.com.denram.empreendimentos_sc_api.domain.Empreendimento;
import br.com.denram.empreendimentos_sc_api.dto.EmpreendimentoRequestDto;
import br.com.denram.empreendimentos_sc_api.dto.EmpreendimentoResponseDto;
import br.com.denram.empreendimentos_sc_api.mapper.EmpreendimentoMapper;
import br.com.denram.empreendimentos_sc_api.service.EmpreendimentoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmpreendimentoController")
class EmpreendimentoControllerTest {

    @Mock
    private EmpreendimentoService empreendimentoService;

    @Mock
    private EmpreendimentoMapper empreendimentoMapper;

    @InjectMocks
    private EmpreendimentoController empreendimentoController;

    @Nested
    @DisplayName("criar")
    class Criar {

        @Test
        @DisplayName("deve criar empreendimento e retornar dto")
        void deveCriarEmpreendimentoERetornarDto() {
            EmpreendimentoRequestDto requestDto = mock(EmpreendimentoRequestDto.class);
            Empreendimento entity = novoEmpreendimento(1L, "Empreendimento Alpha");
            EmpreendimentoResponseDto responseDto = novoResponseDto(1L, "Empreendimento Alpha");

            when(empreendimentoService.criar(requestDto)).thenReturn(entity);
            when(empreendimentoMapper.toResponseDto(entity)).thenReturn(responseDto);

            EmpreendimentoResponseDto resultado = empreendimentoController.criar(requestDto);

            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertSame(responseDto, resultado)
            );

            verify(empreendimentoService).criar(requestDto);
            verify(empreendimentoMapper).toResponseDto(entity);
            verifyNoMoreInteractions(empreendimentoService, empreendimentoMapper);
        }
    }

    @Nested
    @DisplayName("atualizar")
    class Atualizar {

        @Test
        @DisplayName("deve atualizar empreendimento e retornar dto")
        void deveAtualizarEmpreendimentoERetornarDto() {
            Long id = 10L;
            EmpreendimentoRequestDto requestDto = mock(EmpreendimentoRequestDto.class);
            Empreendimento entity = novoEmpreendimento(id, "Empreendimento Atualizado");
            EmpreendimentoResponseDto responseDto = novoResponseDto(id, "Empreendimento Atualizado");

            when(empreendimentoService.atualizar(id, requestDto)).thenReturn(entity);
            when(empreendimentoMapper.toResponseDto(entity)).thenReturn(responseDto);

            EmpreendimentoResponseDto resultado = empreendimentoController.atualizar(id, requestDto);

            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertSame(responseDto, resultado)
            );

            verify(empreendimentoService).atualizar(id, requestDto);
            verify(empreendimentoMapper).toResponseDto(entity);
            verifyNoMoreInteractions(empreendimentoService, empreendimentoMapper);
        }
    }

    @Nested
    @DisplayName("buscarPorId")
    class BuscarPorId {

        @Test
        @DisplayName("deve buscar por id e retornar dto")
        void deveBuscarPorIdERetornarDto() {
            Long id = 20L;
            Empreendimento entity = novoEmpreendimento(id, "Empreendimento Busca");
            EmpreendimentoResponseDto responseDto = novoResponseDto(id, "Empreendimento Busca");

            when(empreendimentoService.buscarPorId(id)).thenReturn(entity);
            when(empreendimentoMapper.toResponseDto(entity)).thenReturn(responseDto);

            EmpreendimentoResponseDto resultado = empreendimentoController.buscarPorId(id);

            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertSame(responseDto, resultado)
            );

            verify(empreendimentoService).buscarPorId(id);
            verify(empreendimentoMapper).toResponseDto(entity);
            verifyNoMoreInteractions(empreendimentoService, empreendimentoMapper);
        }
    }

    @Nested
    @DisplayName("buscar")
    class Buscar {

        @Test
        @DisplayName("deve buscar empreendimentos e mapear página para dto")
        void deveBuscarEmpreendimentosEMapearPaginaParaDto() {
            String nome = "alpha";
            String municipioNome = "joinville";
            String segmento = "SERVICOS";
            Pageable pageable = PageRequest.of(0, 10);

            Empreendimento entity1 = novoEmpreendimento(1L, "Alpha 1");
            Empreendimento entity2 = novoEmpreendimento(2L, "Alpha 2");

            EmpreendimentoResponseDto dto1 = novoResponseDto(1L, "Alpha 1");
            EmpreendimentoResponseDto dto2 = novoResponseDto(2L, "Alpha 2");

            Page<Empreendimento> pageEntity = new PageImpl<>(List.of(entity1, entity2), pageable, 2);

            when(empreendimentoService.buscar(nome, municipioNome, segmento, pageable)).thenReturn(pageEntity);
            when(empreendimentoMapper.toResponseDto(entity1)).thenReturn(dto1);
            when(empreendimentoMapper.toResponseDto(entity2)).thenReturn(dto2);

            Page<EmpreendimentoResponseDto> resultado =
                    empreendimentoController.buscar(nome, municipioNome, segmento, pageable);

            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertEquals(2, resultado.getTotalElements()),
                    () -> assertEquals(2, resultado.getContent().size()),
                    () -> assertSame(dto1, resultado.getContent().get(0)),
                    () -> assertSame(dto2, resultado.getContent().get(1)),
                    () -> assertEquals(0, resultado.getNumber()),
                    () -> assertEquals(10, resultado.getSize())
            );

            verify(empreendimentoService).buscar(nome, municipioNome, segmento, pageable);
            verify(empreendimentoMapper).toResponseDto(entity1);
            verify(empreendimentoMapper).toResponseDto(entity2);
            verifyNoMoreInteractions(empreendimentoService, empreendimentoMapper);
        }

        @ParameterizedTest(name = "deve buscar com nome={0}, municipioNome={1}, segmento={2}")
        @CsvSource({
                "alpha, joinville, SERVICOS",
                "beta, blumenau, COMERCIO",
                "'', '', ''"
        })
        @DisplayName("deve repassar os filtros para a service")
        void deveRepassarOsFiltrosParaAService(String nome, String municipioNome, String segmento) {
            Pageable pageable = PageRequest.of(1, 5);
            Page<Empreendimento> pageEntity = new PageImpl<>(List.of(), pageable, 0);

            String nomeParam = normalizarCsvVazio(nome);
            String municipioNomeParam = normalizarCsvVazio(municipioNome);
            String segmentoParam = normalizarCsvVazio(segmento);

            when(empreendimentoService.buscar(nomeParam, municipioNomeParam, segmentoParam, pageable))
                    .thenReturn(pageEntity);

            Page<EmpreendimentoResponseDto> resultado =
                    empreendimentoController.buscar(nomeParam, municipioNomeParam, segmentoParam, pageable);

            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertTrue(resultado.getContent().isEmpty()),
                    () -> assertEquals(0, resultado.getTotalElements())
            );

            verify(empreendimentoService).buscar(nomeParam, municipioNomeParam, segmentoParam, pageable);
            verifyNoMoreInteractions(empreendimentoService);
            verifyNoInteractions(empreendimentoMapper);
        }
    }

    @Nested
    @DisplayName("excluir")
    class Excluir {

        @Test
        @DisplayName("deve excluir empreendimento pelo id")
        void deveExcluirEmpreendimentoPeloId() {
            Long id = 30L;

            assertDoesNotThrow(() -> empreendimentoController.excluir(id));

            verify(empreendimentoService).excluir(id);
            verifyNoMoreInteractions(empreendimentoService);
            verifyNoInteractions(empreendimentoMapper);
        }
    }

    private static Empreendimento novoEmpreendimento(Long id, String nome) {
        Empreendimento entity = new Empreendimento();
        entity.setId(id);
        entity.setNome(nome);
        return entity;
    }

    private static EmpreendimentoResponseDto novoResponseDto(Long id, String nome) {
        return EmpreendimentoResponseDto.builder()
                .id(id)
                .nome(nome)
                .build();
    }

    private static String normalizarCsvVazio(String valor) {
        return valor != null && valor.isBlank() ? null : valor;
    }
}