package br.com.denram.empreendimentos_sc_api.service;

import br.com.denram.empreendimentos_sc_api.domain.Empreendimento;
import br.com.denram.empreendimentos_sc_api.domain.Municipio;
import br.com.denram.empreendimentos_sc_api.domain.Segmento;
import br.com.denram.empreendimentos_sc_api.dto.EmpreendimentoRequestDto;
import br.com.denram.empreendimentos_sc_api.exception.ResourceNotFoundException;
import br.com.denram.empreendimentos_sc_api.repository.EmpreendimentoRepository;
import br.com.denram.empreendimentos_sc_api.repository.MunicipioRepository;
import br.com.denram.empreendimentos_sc_api.specification.EmpreendimentoSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmpreendimentoService")
class EmpreendimentoServiceTest {

    @Mock
    private EmpreendimentoRepository empreendimentoRepository;

    @Mock
    private MunicipioRepository municipioRepository;

    @InjectMocks
    private EmpreendimentoService empreendimentoService;

    @Nested
    @DisplayName("criar")
    class Criar {

        @Test
        @DisplayName("deve criar empreendimento preenchendo os dados corretamente")
        void deveCriarEmpreendimentoComSucesso() {
            Long municipioId = 10L;
            Municipio municipio = novoMunicipio(municipioId, "Joinville");
            EmpreendimentoRequestDto dto = mock(EmpreendimentoRequestDto.class);
            Segmento segmento = primeiroSegmentoDisponivel();

            mockDto(dto,
                    "Empreendimento A",
                    "Responsável A",
                    municipioId,
                    "segmento qualquer",
                    "contato@empresa.com",
                    "47999999999",
                    Boolean.TRUE);

            when(municipioRepository.findById(municipioId)).thenReturn(Optional.of(municipio));
            when(empreendimentoRepository.save(any(Empreendimento.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            try (MockedStatic<Segmento> segmentoMock = mockStatic(Segmento.class)) {
                segmentoMock.when(() -> Segmento.fromNome("segmento qualquer")).thenReturn(segmento);

                Empreendimento resultado = empreendimentoService.criar(dto);

                ArgumentCaptor<Empreendimento> captor = ArgumentCaptor.forClass(Empreendimento.class);
                verify(empreendimentoRepository).save(captor.capture());

                Empreendimento salvo = captor.getValue();
                assertAll(
                        () -> assertEquals("Empreendimento A", salvo.getNome()),
                        () -> assertEquals("Responsável A", salvo.getNomeResponsavel()),
                        () -> assertSame(municipio, salvo.getMunicipio()),
                        () -> assertEquals(segmento, salvo.getSegmento()),
                        () -> assertEquals("contato@empresa.com", salvo.getEmail()),
                        () -> assertEquals("47999999999", salvo.getTelefone()),
                        () -> assertTrue(salvo.getAtivo()),
                        () -> assertSame(salvo, resultado)
                );

                verify(municipioRepository).findById(municipioId);
            }
        }

        @Test
        @DisplayName("deve definir ativo como true quando dto.ativo for nulo")
        void deveDefinirAtivoComoTrueQuandoNaoInformado() {
            Long municipioId = 20L;
            Municipio municipio = novoMunicipio(municipioId, "Blumenau");
            EmpreendimentoRequestDto dto = mock(EmpreendimentoRequestDto.class);
            Segmento segmento = primeiroSegmentoDisponivel();

            mockDto(dto,
                    "Empreendimento B",
                    "Responsável B",
                    municipioId,
                    "segmento qualquer",
                    "b@empresa.com",
                    "47888888888",
                    null);

            when(municipioRepository.findById(municipioId)).thenReturn(Optional.of(municipio));
            when(empreendimentoRepository.save(any(Empreendimento.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            try (MockedStatic<Segmento> segmentoMock = mockStatic(Segmento.class)) {
                segmentoMock.when(() -> Segmento.fromNome("segmento qualquer")).thenReturn(segmento);

                empreendimentoService.criar(dto);

                ArgumentCaptor<Empreendimento> captor = ArgumentCaptor.forClass(Empreendimento.class);
                verify(empreendimentoRepository).save(captor.capture());

                assertTrue(captor.getValue().getAtivo());
            }
        }

        @Test
        @DisplayName("deve lançar exceção quando município não existir")
        void deveLancarExcecaoQuandoMunicipioNaoExistir() {
            Long municipioId = 99L;
            EmpreendimentoRequestDto dto = mock(EmpreendimentoRequestDto.class);

            when(dto.getMunicipioId()).thenReturn(municipioId);
            when(municipioRepository.findById(municipioId)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrowsExactly(
                    ResourceNotFoundException.class,
                    () -> empreendimentoService.criar(dto)
            );

            assertEquals("Município não encontrado para o id: 99", exception.getMessage());
            verify(empreendimentoRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("atualizar")
    class Atualizar {

        @Test
        @DisplayName("deve atualizar empreendimento existente")
        void deveAtualizarEmpreendimentoComSucesso() {
            Long empreendimentoId = 1L;
            Long municipioId = 2L;

            Empreendimento existente = novoEmpreendimento(empreendimentoId, "Antigo");
            Municipio municipio = novoMunicipio(municipioId, "Florianópolis");
            EmpreendimentoRequestDto dto = mock(EmpreendimentoRequestDto.class);
            Segmento segmento = primeiroSegmentoDisponivel();

            mockDto(dto,
                    "Novo Nome",
                    "Novo Responsável",
                    municipioId,
                    "segmento atualizado",
                    "novo@empresa.com",
                    "47777777777",
                    Boolean.FALSE);

            when(empreendimentoRepository.findById(empreendimentoId)).thenReturn(Optional.of(existente));
            when(municipioRepository.findById(municipioId)).thenReturn(Optional.of(municipio));
            when(empreendimentoRepository.save(any(Empreendimento.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            try (MockedStatic<Segmento> segmentoMock = mockStatic(Segmento.class)) {
                segmentoMock.when(() -> Segmento.fromNome("segmento atualizado")).thenReturn(segmento);

                Empreendimento resultado = empreendimentoService.atualizar(empreendimentoId, dto);

                assertAll(
                        () -> assertSame(existente, resultado),
                        () -> assertEquals("Novo Nome", existente.getNome()),
                        () -> assertEquals("Novo Responsável", existente.getNomeResponsavel()),
                        () -> assertSame(municipio, existente.getMunicipio()),
                        () -> assertEquals(segmento, existente.getSegmento()),
                        () -> assertEquals("novo@empresa.com", existente.getEmail()),
                        () -> assertEquals("47777777777", existente.getTelefone()),
                        () -> assertFalse(existente.getAtivo())
                );

                verify(empreendimentoRepository).save(existente);
            }
        }

        @Test
        @DisplayName("deve lançar exceção quando empreendimento não existir")
        void deveLancarExcecaoQuandoEmpreendimentoNaoExistir() {
            Long empreendimentoId = 123L;
            EmpreendimentoRequestDto dto = mock(EmpreendimentoRequestDto.class);

            when(empreendimentoRepository.findById(empreendimentoId)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrowsExactly(
                    ResourceNotFoundException.class,
                    () -> empreendimentoService.atualizar(empreendimentoId, dto)
            );

            assertEquals("Empreendimento não encontrado para o id: 123", exception.getMessage());
            verify(municipioRepository, never()).findById(any());
            verify(empreendimentoRepository, never()).save(any());
        }

        @Test
        @DisplayName("deve lançar exceção quando município não existir na atualização")
        void deveLancarExcecaoQuandoMunicipioNaoExistirNaAtualizacao() {
            Long empreendimentoId = 1L;
            Long municipioId = 999L;

            Empreendimento existente = novoEmpreendimento(empreendimentoId, "Existente");
            EmpreendimentoRequestDto dto = mock(EmpreendimentoRequestDto.class);

            when(dto.getMunicipioId()).thenReturn(municipioId);
            when(empreendimentoRepository.findById(empreendimentoId)).thenReturn(Optional.of(existente));
            when(municipioRepository.findById(municipioId)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrowsExactly(
                    ResourceNotFoundException.class,
                    () -> empreendimentoService.atualizar(empreendimentoId, dto)
            );

            assertEquals("Município não encontrado para o id: 999", exception.getMessage());
            verify(empreendimentoRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("buscarPorId")
    class BuscarPorId {

        @Test
        @DisplayName("deve retornar empreendimento quando existir")
        void deveRetornarEmpreendimentoQuandoExistir() {
            Long id = 5L;
            Empreendimento empreendimento = novoEmpreendimento(id, "Empreendimento X");

            when(empreendimentoRepository.findById(id)).thenReturn(Optional.of(empreendimento));

            Empreendimento resultado = empreendimentoService.buscarPorId(id);

            assertSame(empreendimento, resultado);
        }

        @Test
        @DisplayName("deve lançar exceção quando não existir")
        void deveLancarExcecaoQuandoNaoExistir() {
            Long id = 6L;
            when(empreendimentoRepository.findById(id)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrowsExactly(
                    ResourceNotFoundException.class,
                    () -> empreendimentoService.buscarPorId(id)
            );

            assertEquals("Empreendimento não encontrado para o id: 6", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("buscar")
    class Buscar {

        @Test
        @DisplayName("deve buscar empreendimentos aplicando specification e paginação")
        void deveBuscarComFiltros() {
            String nome = "alpha";
            String municipioNome = "join";
            String segmentoNome = "segmento filtro";
            Pageable pageable = PageRequest.of(0, 10);

            Specification<Empreendimento> specNome = (root, query, cb) -> cb.conjunction();
            Specification<Empreendimento> specMunicipio = (root, query, cb) -> cb.conjunction();
            Specification<Empreendimento> specSegmento = (root, query, cb) -> cb.conjunction();

            Segmento segmento = primeiroSegmentoDisponivel();
            Page<Empreendimento> page = new PageImpl<>(List.of(novoEmpreendimento(1L, "Alpha")));

            try (MockedStatic<Segmento> segmentoMock = mockStatic(Segmento.class);
                 MockedStatic<EmpreendimentoSpecification> specificationMock = mockStatic(EmpreendimentoSpecification.class)) {

                segmentoMock.when(() -> Segmento.fromNome(segmentoNome)).thenReturn(segmento);
                specificationMock.when(() -> EmpreendimentoSpecification.nomeContendo(nome)).thenReturn(specNome);
                specificationMock.when(() -> EmpreendimentoSpecification.municipioNomeContendo(municipioNome)).thenReturn(specMunicipio);
                specificationMock.when(() -> EmpreendimentoSpecification.segmentoIgual(segmento)).thenReturn(specSegmento);

                when(empreendimentoRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

                Page<Empreendimento> resultado = empreendimentoService.buscar(nome, municipioNome, segmentoNome, pageable);

                assertSame(page, resultado);

                segmentoMock.verify(() -> Segmento.fromNome(segmentoNome));
                specificationMock.verify(() -> EmpreendimentoSpecification.nomeContendo(nome));
                specificationMock.verify(() -> EmpreendimentoSpecification.municipioNomeContendo(municipioNome));
                specificationMock.verify(() -> EmpreendimentoSpecification.segmentoIgual(segmento));
                verify(empreendimentoRepository).findAll(any(Specification.class), eq(pageable));
            }
        }

        @ParameterizedTest(name = "deve repassar o segmento \"{0}\" para Segmento.fromNome")
        @MethodSource("br.com.denram.empreendimentos_sc_api.service.EmpreendimentoServiceTest#segmentosParaBusca")
        void deveRepassarSegmentoInformadoParaConversao(String segmentoInformado) {
            Pageable pageable = PageRequest.of(0, 5);
            Page<Empreendimento> page = Page.empty();
            Segmento segmento = primeiroSegmentoDisponivel();

            Specification<Empreendimento> specNeutra = (root, query, cb) -> cb.conjunction();

            try (MockedStatic<Segmento> segmentoMock = mockStatic(Segmento.class);
                 MockedStatic<EmpreendimentoSpecification> specificationMock = mockStatic(EmpreendimentoSpecification.class)) {

                segmentoMock.when(() -> Segmento.fromNome(segmentoInformado)).thenReturn(segmento);
                specificationMock.when(() -> EmpreendimentoSpecification.nomeContendo(null)).thenReturn(specNeutra);
                specificationMock.when(() -> EmpreendimentoSpecification.municipioNomeContendo(null)).thenReturn(specNeutra);
                specificationMock.when(() -> EmpreendimentoSpecification.segmentoIgual(segmento)).thenReturn(specNeutra);

                when(empreendimentoRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

                Page<Empreendimento> resultado = empreendimentoService.buscar(null, null, segmentoInformado, pageable);

                assertSame(page, resultado);
                segmentoMock.verify(() -> Segmento.fromNome(segmentoInformado));
                specificationMock.verify(() -> EmpreendimentoSpecification.nomeContendo(null));
                specificationMock.verify(() -> EmpreendimentoSpecification.municipioNomeContendo(null));
                specificationMock.verify(() -> EmpreendimentoSpecification.segmentoIgual(segmento));
                verify(empreendimentoRepository).findAll(any(Specification.class), eq(pageable));
            }
        }
    }

    @Nested
    @DisplayName("excluir")
    class Excluir {

        @Test
        @DisplayName("deve excluir empreendimento existente")
        void deveExcluirEmpreendimentoExistente() {
            Long id = 8L;
            Empreendimento empreendimento = novoEmpreendimento(id, "Excluir");

            when(empreendimentoRepository.findById(id)).thenReturn(Optional.of(empreendimento));

            empreendimentoService.excluir(id);

            verify(empreendimentoRepository).delete(empreendimento);
        }

        @Test
        @DisplayName("deve lançar exceção quando empreendimento a excluir não existir")
        void deveLancarExcecaoQuandoEmpreendimentoAExcluirNaoExistir() {
            Long id = 9L;
            when(empreendimentoRepository.findById(id)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrowsExactly(
                    ResourceNotFoundException.class,
                    () -> empreendimentoService.excluir(id)
            );

            assertEquals("Empreendimento não encontrado para o id: 9", exception.getMessage());
            verify(empreendimentoRepository, never()).delete(any(Empreendimento.class));
        }
    }

    static Stream<String> segmentosParaBusca() {
        return Stream.of("INDUSTRIA", "SERVICOS", "COMERCIO", "qualquer-segmento");
    }

    private static void mockDto(EmpreendimentoRequestDto dto,
                                String nome,
                                String nomeResponsavel,
                                Long municipioId,
                                String segmento,
                                String email,
                                String telefone,
                                Boolean ativo) {
        when(dto.getNome()).thenReturn(nome);
        when(dto.getNomeResponsavel()).thenReturn(nomeResponsavel);
        when(dto.getMunicipioId()).thenReturn(municipioId);
        when(dto.getSegmento()).thenReturn(segmento);
        when(dto.getEmail()).thenReturn(email);
        when(dto.getTelefone()).thenReturn(telefone);
        when(dto.getAtivo()).thenReturn(ativo);
    }

    private static Municipio novoMunicipio(Long id, String nome) {
        return new Municipio(id, nome);
    }

    private static Empreendimento novoEmpreendimento(Long id, String nome) {
        Empreendimento empreendimento = new Empreendimento();
        empreendimento.setId(id);
        empreendimento.setNome(nome);
        return empreendimento;
    }

    private static Segmento primeiroSegmentoDisponivel() {
        Segmento[] valores = Segmento.values();
        if (valores.length == 0) {
            throw new IllegalStateException("O enum Segmento precisa ter ao menos um valor para os testes.");
        }
        return valores[0];
    }
}