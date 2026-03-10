package br.com.denram.empreendimentos_sc_api.controller;

import br.com.denram.empreendimentos_sc_api.domain.Municipio;
import br.com.denram.empreendimentos_sc_api.dto.MunicipioResponseDto;
import br.com.denram.empreendimentos_sc_api.mapper.MunicipioMapper;
import br.com.denram.empreendimentos_sc_api.repository.MunicipioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("MunicipioController")
class MunicipioControllerTest {

    @Mock
    private MunicipioRepository municipioRepository;

    @Mock
    private MunicipioMapper municipioMapper;

    @InjectMocks
    private MunicipioController municipioController;

    @Nested
    @DisplayName("listar")
    class Listar {

        @Test
        @DisplayName("deve listar municípios com parâmetros padrão")
        void deveListarMunicipiosComParametrosPadrao() {
            Page<Municipio> municipiosPage = new PageImpl<>(List.of(novoMunicipio(1L, "Joinville")));
            Page<MunicipioResponseDto> dtoPage = new PageImpl<>(List.of(novoDto(1L, "Joinville")));

            when(municipioRepository.findAll(any(Pageable.class))).thenReturn(municipiosPage);
            when(municipioMapper.toDtoPage(municipiosPage)).thenReturn(dtoPage);

            Page<MunicipioResponseDto> resultado = municipioController.listar(0, 20, "nome", "asc");

            ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
            verify(municipioRepository).findAll(pageableCaptor.capture());
            verify(municipioMapper).toDtoPage(municipiosPage);

            Pageable pageable = pageableCaptor.getValue();

            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertSame(dtoPage, resultado),
                    () -> assertEquals(0, pageable.getPageNumber()),
                    () -> assertEquals(20, pageable.getPageSize()),
                    () -> assertEquals(Sort.Direction.ASC, pageable.getSort().getOrderFor("nome").getDirection())
            );
        }

        @ParameterizedTest(name = "deve listar usando page={0}, size={1}, sort={2}, direction={3}")
        @CsvSource({
                "0, 10, nome, asc, ASC",
                "2, 5, id, desc, DESC",
                "1, 50, nome, DESC, DESC"
        })
        void deveListarMunicipiosComPaginacaoEOrdenacaoInformadas(int page,
                                                                  int size,
                                                                  String sort,
                                                                  String direction,
                                                                  Sort.Direction expectedDirection) {
            Page<Municipio> municipiosPage = new PageImpl<>(List.of(novoMunicipio(2L, "Blumenau")));
            Page<MunicipioResponseDto> dtoPage = new PageImpl<>(List.of(novoDto(2L, "Blumenau")));

            when(municipioRepository.findAll(any(Pageable.class))).thenReturn(municipiosPage);
            when(municipioMapper.toDtoPage(municipiosPage)).thenReturn(dtoPage);

            Page<MunicipioResponseDto> resultado = municipioController.listar(page, size, sort, direction);

            ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
            verify(municipioRepository).findAll(pageableCaptor.capture());
            verify(municipioMapper).toDtoPage(municipiosPage);

            Pageable pageable = pageableCaptor.getValue();

            assertAll(
                    () -> assertSame(dtoPage, resultado),
                    () -> assertEquals(page, pageable.getPageNumber()),
                    () -> assertEquals(size, pageable.getPageSize()),
                    () -> assertEquals(expectedDirection, pageable.getSort().getOrderFor(sort).getDirection())
            );
        }

        @Test
        @DisplayName("deve propagar IllegalArgumentException quando direction for inválida")
        void devePropagarIllegalArgumentExceptionQuandoDirectionForInvalida() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> municipioController.listar(0, 20, "nome", "invalida")
            );

            assertNotNull(exception);
            verifyNoInteractions(municipioRepository, municipioMapper);
        }
    }

    @Nested
    @DisplayName("buscarPorNome")
    class BuscarPorNome {

        @Test
        @DisplayName("deve buscar municípios por nome com parâmetros padrão")
        void deveBuscarMunicipiosPorNomeComParametrosPadrao() {
            String nome = "join";
            Page<Municipio> municipiosPage = new PageImpl<>(List.of(novoMunicipio(1L, "Joinville")));
            Page<MunicipioResponseDto> dtoPage = new PageImpl<>(List.of(novoDto(1L, "Joinville")));

            when(municipioRepository.findByNomeContainingIgnoreCase(eq(nome), any(Pageable.class)))
                    .thenReturn(municipiosPage);
            when(municipioMapper.toDtoPage(municipiosPage)).thenReturn(dtoPage);

            Page<MunicipioResponseDto> resultado = municipioController.buscarPorNome(nome, 0, 20, "nome", "asc");

            ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
            verify(municipioRepository).findByNomeContainingIgnoreCase(eq(nome), pageableCaptor.capture());
            verify(municipioMapper).toDtoPage(municipiosPage);

            Pageable pageable = pageableCaptor.getValue();

            assertAll(
                    () -> assertNotNull(resultado),
                    () -> assertSame(dtoPage, resultado),
                    () -> assertEquals(0, pageable.getPageNumber()),
                    () -> assertEquals(20, pageable.getPageSize()),
                    () -> assertEquals(Sort.Direction.ASC, pageable.getSort().getOrderFor("nome").getDirection())
            );
        }

        @ParameterizedTest(name = "deve buscar nome={0} com page={1}, size={2}, sort={3}, direction={4}")
        @CsvSource({
                "join, 0, 10, nome, asc, ASC",
                "flor, 1, 15, id, desc, DESC",
                "blume, 3, 25, nome, DESC, DESC"
        })
        void deveBuscarMunicipiosPorNomeComPaginacaoEOrdenacaoInformadas(String nome,
                                                                         int page,
                                                                         int size,
                                                                         String sort,
                                                                         String direction,
                                                                         Sort.Direction expectedDirection) {
            Page<Municipio> municipiosPage = new PageImpl<>(List.of(novoMunicipio(3L, "Florianópolis")));
            Page<MunicipioResponseDto> dtoPage = new PageImpl<>(List.of(novoDto(3L, "Florianópolis")));

            when(municipioRepository.findByNomeContainingIgnoreCase(eq(nome), any(Pageable.class)))
                    .thenReturn(municipiosPage);
            when(municipioMapper.toDtoPage(municipiosPage)).thenReturn(dtoPage);

            Page<MunicipioResponseDto> resultado = municipioController.buscarPorNome(nome, page, size, sort, direction);

            ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
            verify(municipioRepository).findByNomeContainingIgnoreCase(eq(nome), pageableCaptor.capture());
            verify(municipioMapper).toDtoPage(municipiosPage);

            Pageable pageable = pageableCaptor.getValue();

            assertAll(
                    () -> assertSame(dtoPage, resultado),
                    () -> assertEquals(page, pageable.getPageNumber()),
                    () -> assertEquals(size, pageable.getPageSize()),
                    () -> assertEquals(expectedDirection, pageable.getSort().getOrderFor(sort).getDirection())
            );
        }

        @Test
        @DisplayName("deve propagar IllegalArgumentException quando direction for inválida")
        void devePropagarIllegalArgumentExceptionQuandoDirectionForInvalida() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> municipioController.buscarPorNome("join", 0, 20, "nome", "invalida")
            );

            assertNotNull(exception);
            verifyNoInteractions(municipioRepository, municipioMapper);
        }
    }

    private static Municipio novoMunicipio(Long id, String nome) {
        Municipio municipio = new Municipio(id, nome);
        return municipio;
    }

    private static MunicipioResponseDto novoDto(Long id, String nome) {
        MunicipioResponseDto dto = new MunicipioResponseDto(id, nome);
        return dto;
    }
}