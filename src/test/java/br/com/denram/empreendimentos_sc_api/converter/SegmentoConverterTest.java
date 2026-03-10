package br.com.denram.empreendimentos_sc_api.converter;

import br.com.denram.empreendimentos_sc_api.domain.Segmento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

@DisplayName("SegmentoConverter")
class SegmentoConverterTest {

    private final SegmentoConverter converter = new SegmentoConverter();

    @Nested
    @DisplayName("convertToDatabaseColumn")
    class ConvertToDatabaseColumn {

        @Test
        @DisplayName("deve retornar null quando atributo for null")
        void deveRetornarNullQuandoAtributoForNull() {
            Short resultado = converter.convertToDatabaseColumn(null);

            assertNull(resultado);
        }

        @Test
        @DisplayName("deve converter segmento para índice do banco")
        void deveConverterSegmentoParaIndiceDoBanco() {
            Segmento segmento = primeiroSegmentoDisponivel();

            Short resultado = converter.convertToDatabaseColumn(segmento);

            assertEquals(segmento.getIndex(), resultado);
        }
    }

    @Nested
    @DisplayName("convertToEntityAttribute")
    class ConvertToEntityAttribute {

        @Test
        @DisplayName("deve retornar null quando valor do banco for null")
        void deveRetornarNullQuandoValorDoBancoForNull() {
            Segmento resultado = converter.convertToEntityAttribute(null);

            assertNull(resultado);
        }

        @Test
        @DisplayName("deve converter índice do banco para segmento")
        void deveConverterIndiceDoBancoParaSegmento() {
            short indice = 1;
            Segmento segmento = primeiroSegmentoDisponivel();

            try (MockedStatic<Segmento> segmentoMock = mockStatic(Segmento.class)) {
                segmentoMock.when(() -> Segmento.fromIndex(indice)).thenReturn(segmento);

                Segmento resultado = converter.convertToEntityAttribute(indice);

                assertSame(segmento, resultado);
                segmentoMock.verify(() -> Segmento.fromIndex(indice));
            }
        }

        @Test
        @DisplayName("deve propagar exceção quando índice for inválido")
        void devePropagarExcecaoQuandoIndiceForInvalido() {
            short indiceInvalido = 99;

            try (MockedStatic<Segmento> segmentoMock = mockStatic(Segmento.class)) {
                segmentoMock.when(() -> Segmento.fromIndex(indiceInvalido))
                        .thenThrow(new IllegalArgumentException("Índice de segmento inválido: 99"));

                IllegalArgumentException exception = assertThrows(
                        IllegalArgumentException.class,
                        () -> converter.convertToEntityAttribute(indiceInvalido)
                );

                assertEquals("Índice de segmento inválido: 99", exception.getMessage());
                segmentoMock.verify(() -> Segmento.fromIndex(indiceInvalido));
            }
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