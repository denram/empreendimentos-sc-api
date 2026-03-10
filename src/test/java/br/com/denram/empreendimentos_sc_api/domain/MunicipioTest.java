package br.com.denram.empreendimentos_sc_api.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Municipio")
class MunicipioTest {

    @Nested
    @DisplayName("construtores")
    class Construtores {

        @Test
        @DisplayName("deve criar instância com construtor sem argumentos")
        void deveCriarInstanciaComConstrutorSemArgumentos() {
            Municipio municipio = new Municipio();

            assertNotNull(municipio);
            assertAll(
                    () -> assertNull(municipio.getId()),
                    () -> assertNull(municipio.getNome())
            );
        }

        @Test
        @DisplayName("deve criar instância com construtor com todos os argumentos")
        void deveCriarInstanciaComConstrutorComTodosOsArgumentos() {
            Municipio municipio = new Municipio(42L, "Joinville");

            assertAll(
                    () -> assertNotNull(municipio),
                    () -> assertEquals(42L, municipio.getId()),
                    () -> assertEquals("Joinville", municipio.getNome())
            );
        }
    }

    @Nested
    @DisplayName("getters")
    class Getters {

        @Test
        @DisplayName("deve retornar os valores informados no construtor")
        void deveRetornarOsValoresInformadosNoConstrutor() {
            Long id = 123L;
            String nome = "Florianópolis";

            Municipio municipio = new Municipio(id, nome);

            assertAll(
                    () -> assertEquals(id, municipio.getId()),
                    () -> assertEquals(nome, municipio.getNome())
            );
        }

        @Test
        @DisplayName("deve aceitar nome com caracteres acentuados")
        void deveAceitarNomeComCaracteresAcentuados() {
            Municipio municipio = new Municipio(1L, "São José");

            assertAll(
                    () -> assertEquals(1L, municipio.getId()),
                    () -> assertEquals("São José", municipio.getNome())
            );
        }

        @Test
        @DisplayName("deve aceitar nome nulo no estado atual da classe")
        void deveAceitarNomeNuloNoEstadoAtualDaClasse() {
            Municipio municipio = new Municipio(10L, null);

            assertAll(
                    () -> assertEquals(10L, municipio.getId()),
                    () -> assertNull(municipio.getNome())
            );
        }

        @Test
        @DisplayName("deve aceitar id nulo no estado atual da classe")
        void deveAceitarIdNuloNoEstadoAtualDaClasse() {
            Municipio municipio = new Municipio(null, "Blumenau");

            assertAll(
                    () -> assertNull(municipio.getId()),
                    () -> assertEquals("Blumenau", municipio.getNome())
            );
        }
    }
}