package br.com.denram.empreendimentos_sc_api.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@DisplayName("Segmento")
class SegmentoTest {

    @Nested
    @DisplayName("getIndex")
    class GetIndex {

        @ParameterizedTest(name = "{0} deve possuir índice {1}")
        @CsvSource({
                "TECNOLOGIA, 0",
                "COMERCIO, 1",
                "INDUSTRIA, 2",
                "SERVICOS, 3",
                "AGRONEGOCIO, 4"
        })
        void deveRetornarIndiceCorreto(Segmento segmento, short indiceEsperado) {
            assertEquals(indiceEsperado, segmento.getIndex());
        }
    }

    @Nested
    @DisplayName("fromIndex")
    class FromIndex {

        @Test
        @DisplayName("deve retornar null quando índice for null")
        void deveRetornarNullQuandoIndiceForNull() {
            Segmento resultado = Segmento.fromIndex(null);

            assertNull(resultado);
        }

        @ParameterizedTest(name = "índice {0} deve retornar {1}")
        @CsvSource({
                "0, TECNOLOGIA",
                "1, COMERCIO",
                "2, INDUSTRIA",
                "3, SERVICOS",
                "4, AGRONEGOCIO"
        })
        void deveRetornarSegmentoCorretoQuandoIndiceForValido(short indice, Segmento segmentoEsperado) {
            Segmento resultado = Segmento.fromIndex(indice);

            assertSame(segmentoEsperado, resultado);
        }

        @ParameterizedTest(name = "deve lançar exceção para índice inválido {0}")
        @ValueSource(shorts = {-1, 5, 9, 100})
        void deveLancarExcecaoQuandoIndiceForInvalido(short indiceInvalido) {
            IllegalArgumentException exception = assertThrowsExactly(
                    IllegalArgumentException.class,
                    () -> Segmento.fromIndex(indiceInvalido)
            );

            assertEquals("Segmento inválido: " + indiceInvalido, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("fromNome")
    class FromNome {

        @Test
        @DisplayName("deve retornar null quando nome for null")
        void deveRetornarNullQuandoNomeForNull() {
            Segmento resultado = Segmento.fromNome(null);

            assertNull(resultado);
        }

        @ParameterizedTest(name = "deve retornar null quando nome for vazio/branco: \"{0}\"")
        @NullAndEmptySource
        @ValueSource(strings = {" ", "   ", "\t", "\n"})
        void deveRetornarNullQuandoNomeForVazioOuBranco(String nome) {
            Segmento resultado = Segmento.fromNome(nome);

            assertNull(resultado);
        }

        @ParameterizedTest(name = "\"{0}\" deve retornar {1}")
        @CsvSource({
                "TECNOLOGIA, TECNOLOGIA",
                "tecnologia, TECNOLOGIA",
                "'  tecnologia  ', TECNOLOGIA",
                "COMERCIO, COMERCIO",
                "comercio, COMERCIO",
                "INDUSTRIA, INDUSTRIA",
                "industria, INDUSTRIA",
                "SERVICOS, SERVICOS",
                "servicos, SERVICOS",
                "AGRONEGOCIO, AGRONEGOCIO",
                "agronegocio, AGRONEGOCIO"
        })
        void deveRetornarSegmentoCorretoQuandoNomeForValido(String nome, Segmento segmentoEsperado) {
            Segmento resultado = Segmento.fromNome(nome);

            assertSame(segmentoEsperado, resultado);
        }

        @ParameterizedTest(name = "deve lançar exceção para nome inválido \"{0}\"")
        @ValueSource(strings = {
                "TEC",
                "COMÉRCIO",
                "INDÚSTRIA",
                "SERVIÇOS",
                "AGRO",
                "outro"
        })
        void deveLancarExcecaoQuandoNomeForInvalido(String nomeInvalido) {
            IllegalArgumentException exception = assertThrowsExactly(
                    IllegalArgumentException.class,
                    () -> Segmento.fromNome(nomeInvalido)
            );

            assertEquals("Segmento inválido: " + nomeInvalido, exception.getMessage());
        }
    }
}