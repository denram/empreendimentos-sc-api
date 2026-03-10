package br.com.denram.empreendimentos_sc_api.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Empreendimento")
class EmpreendimentoTest {

    @Nested
    @DisplayName("Lombok / estrutura básica")
    class EstruturaBasica {

        @Test
        @DisplayName("deve criar instância com construtor sem argumentos")
        void deveCriarInstanciaComConstrutorSemArgumentos() {
            Empreendimento entity = new Empreendimento();

            assertNotNull(entity);
            assertAll(
                    () -> assertNull(entity.getId()),
                    () -> assertNull(entity.getNome()),
                    () -> assertNull(entity.getNomeResponsavel()),
                    () -> assertNull(entity.getMunicipio()),
                    () -> assertNull(entity.getSegmento()),
                    () -> assertNull(entity.getEmail()),
                    () -> assertNull(entity.getTelefone()),
                    () -> assertNull(entity.getAtivo()),
                    () -> assertNull(entity.getCriadoEm()),
                    () -> assertNull(entity.getAtualizadoEm())
            );
        }

        @Test
        @DisplayName("deve permitir ler e escrever todos os atributos")
        void devePermitirLerEEscreverTodosOsAtributos() {
            Empreendimento entity = new Empreendimento();

            Municipio municipio = new Municipio(42L, "Joinville");

            Segmento segmento = primeiroSegmentoDisponivel();
            LocalDateTime criadoEm = LocalDateTime.of(2026, 3, 9, 10, 0, 0);
            LocalDateTime atualizadoEm = LocalDateTime.of(2026, 3, 9, 11, 0, 0);

            entity.setId(1L);
            entity.setNome("Empreendimento Alpha");
            entity.setNomeResponsavel("Denis Ramos");
            entity.setMunicipio(municipio);
            entity.setSegmento(segmento);
            entity.setEmail("alpha@empresa.com");
            entity.setTelefone("47999990000");
            entity.setAtivo(Boolean.TRUE);
            entity.setCriadoEm(criadoEm);
            entity.setAtualizadoEm(atualizadoEm);

            assertAll(
                    () -> assertEquals(1L, entity.getId()),
                    () -> assertEquals("Empreendimento Alpha", entity.getNome()),
                    () -> assertEquals("Denis Ramos", entity.getNomeResponsavel()),
                    () -> assertSame(municipio, entity.getMunicipio()),
                    () -> assertSame(segmento, entity.getSegmento()),
                    () -> assertEquals("alpha@empresa.com", entity.getEmail()),
                    () -> assertEquals("47999990000", entity.getTelefone()),
                    () -> assertTrue(entity.getAtivo()),
                    () -> assertEquals(criadoEm, entity.getCriadoEm()),
                    () -> assertEquals(atualizadoEm, entity.getAtualizadoEm())
            );
        }
    }

    @Nested
    @DisplayName("prePersist")
    class PrePersist {

        @Test
        @DisplayName("deve preencher criadoEm, atualizadoEm e ativo=true quando ativo for null")
        void devePreencherDatasEAtivoTrueQuandoAtivoForNull() {
            Empreendimento entity = new Empreendimento();

            entity.setAtivo(null);

            LocalDateTime antes = LocalDateTime.now().minusSeconds(1);

            entity.prePersist();

            LocalDateTime depois = LocalDateTime.now().plusSeconds(1);

            assertAll(
                    () -> assertNotNull(entity.getCriadoEm()),
                    () -> assertNotNull(entity.getAtualizadoEm()),
                    () -> assertTrue(entity.getAtivo()),
                    () -> assertFalse(entity.getCriadoEm().isBefore(antes)),
                    () -> assertFalse(entity.getCriadoEm().isAfter(depois)),
                    () -> assertFalse(entity.getAtualizadoEm().isBefore(antes)),
                    () -> assertFalse(entity.getAtualizadoEm().isAfter(depois)),
                    () -> assertEquals(entity.getCriadoEm(), entity.getAtualizadoEm())
            );
        }

        @Test
        @DisplayName("deve preservar ativo quando já estiver definido como false")
        void devePreservarAtivoQuandoJaEstiverDefinidoComoFalse() {
            Empreendimento entity = new Empreendimento();
            entity.setAtivo(Boolean.FALSE);

            entity.prePersist();

            assertFalse(entity.getAtivo());
            assertNotNull(entity.getCriadoEm());
            assertNotNull(entity.getAtualizadoEm());
        }

        @Test
        @DisplayName("deve preservar ativo quando já estiver definido como true")
        void devePreservarAtivoQuandoJaEstiverDefinidoComoTrue() {
            Empreendimento entity = new Empreendimento();
            entity.setAtivo(Boolean.TRUE);

            entity.prePersist();

            assertTrue(entity.getAtivo());
            assertNotNull(entity.getCriadoEm());
            assertNotNull(entity.getAtualizadoEm());
        }
    }

    @Nested
    @DisplayName("preUpdate")
    class PreUpdate {

        @Test
        @DisplayName("deve atualizar somente atualizadoEm")
        void deveAtualizarSomenteAtualizadoEm() {
            Empreendimento entity = new Empreendimento();

            LocalDateTime criadoEmOriginal = LocalDateTime.of(2026, 3, 1, 8, 0, 0);
            LocalDateTime atualizadoEmOriginal = LocalDateTime.of(2026, 3, 1, 9, 0, 0);

            entity.setCriadoEm(criadoEmOriginal);
            entity.setAtualizadoEm(atualizadoEmOriginal);

            LocalDateTime antes = LocalDateTime.now().minusSeconds(1);

            entity.preUpdate();

            LocalDateTime depois = LocalDateTime.now().plusSeconds(1);

            assertAll(
                    () -> assertEquals(criadoEmOriginal, entity.getCriadoEm()),
                    () -> assertNotNull(entity.getAtualizadoEm()),
                    () -> assertNotEquals(atualizadoEmOriginal, entity.getAtualizadoEm()),
                    () -> assertFalse(entity.getAtualizadoEm().isBefore(antes)),
                    () -> assertFalse(entity.getAtualizadoEm().isAfter(depois))
            );
        }

        @Test
        @DisplayName("deve definir atualizadoEm mesmo quando valor anterior for null")
        void deveDefinirAtualizadoEmMesmoQuandoValorAnteriorForNull() {
            Empreendimento entity = new Empreendimento();
            entity.setAtualizadoEm(null);

            LocalDateTime antes = LocalDateTime.now().minusSeconds(1);

            entity.preUpdate();

            LocalDateTime depois = LocalDateTime.now().plusSeconds(1);

            assertAll(
                    () -> assertNotNull(entity.getAtualizadoEm()),
                    () -> assertFalse(entity.getAtualizadoEm().isBefore(antes)),
                    () -> assertFalse(entity.getAtualizadoEm().isAfter(depois))
            );
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