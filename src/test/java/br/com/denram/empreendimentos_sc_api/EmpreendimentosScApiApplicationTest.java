package br.com.denram.empreendimentos_sc_api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mockStatic;

@DisplayName("EmpreendimentosScApiApplication")
class EmpreendimentosScApiApplicationTest {

    @Nested
    @DisplayName("main")
    class Main {

        @Test
        @DisplayName("deve iniciar a aplicação repassando os argumentos para SpringApplication.run")
        void deveIniciarAplicacaoRepassandoOsArgumentosParaSpringApplicationRun() {
            String[] args = {"--server.port=8081", "--spring.profiles.active=test"};

            try (MockedStatic<SpringApplication> springApplicationMock = mockStatic(SpringApplication.class)) {
                assertDoesNotThrow(() -> EmpreendimentosScApiApplication.main(args));

                springApplicationMock.verify(
                        () -> SpringApplication.run(EmpreendimentosScApiApplication.class, args)
                );
            }
        }

        @Test
        @DisplayName("deve iniciar a aplicação mesmo quando não houver argumentos")
        void deveIniciarAplicacaoMesmoQuandoNaoHouverArgumentos() {
            String[] args = new String[0];

            try (MockedStatic<SpringApplication> springApplicationMock = mockStatic(SpringApplication.class)) {
                assertDoesNotThrow(() -> EmpreendimentosScApiApplication.main(args));

                springApplicationMock.verify(
                        () -> SpringApplication.run(EmpreendimentosScApiApplication.class, args)
                );
            }
        }

        @Test
        @DisplayName("deve iniciar a aplicação quando args for null")
        void deveIniciarAplicacaoQuandoArgsForNull() {
            String[] args = null;

            try (MockedStatic<SpringApplication> springApplicationMock = mockStatic(SpringApplication.class)) {
                assertDoesNotThrow(() -> EmpreendimentosScApiApplication.main(args));

                springApplicationMock.verify(
                        () -> SpringApplication.run(EmpreendimentosScApiApplication.class, null)
                );
            }
        }
    }
}