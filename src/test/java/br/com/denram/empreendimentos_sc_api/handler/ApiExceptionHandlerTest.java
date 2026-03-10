package br.com.denram.empreendimentos_sc_api.handler;

import br.com.denram.empreendimentos_sc_api.dto.ErrorResponseDto;
import br.com.denram.empreendimentos_sc_api.exception.BusinessException;
import br.com.denram.empreendimentos_sc_api.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApiExceptionHandler")
class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Mock
    private HttpServletRequest request;

    @Nested
    @DisplayName("handleResourceNotFound")
    class HandleResourceNotFound {

        @Test
        @DisplayName("deve retornar 404 com corpo padronizado")
        void deveRetornar404ComCorpoPadronizado() {
            ResourceNotFoundException ex = new ResourceNotFoundException("Empreendimento não encontrado para o id: 1");

            ResponseEntity<ErrorResponseDto> response = handler.handleResourceNotFound(ex, request);

            assertErrorResponse(
                    response,
                    HttpStatus.NOT_FOUND,
                    "Empreendimento não encontrado para o id: 1",
                    0
            );
        }
    }

    @Nested
    @DisplayName("handleBusiness")
    class HandleBusiness {

        @Test
        @DisplayName("deve retornar 400 com corpo padronizado")
        void deveRetornar400ComCorpoPadronizado() {
            BusinessException ex = new BusinessException("Regra de negócio violada.");

            ResponseEntity<ErrorResponseDto> response = handler.handleBusiness(ex, request);

            assertErrorResponse(
                    response,
                    HttpStatus.BAD_REQUEST,
                    "Regra de negócio violada.",
                    0
            );
        }
    }

    @Nested
    @DisplayName("handleValidation")
    class HandleValidation {

        @Test
        @DisplayName("deve retornar 400 com erros de campo mapeados")
        void deveRetornar400ComErrosDeCampoMapeados() {
            DummyRequest target = new DummyRequest();
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(target, "dummyRequest");

            bindingResult.addError(new FieldError("dummyRequest", "nome", "Nome é obrigatório"));
            bindingResult.addError(new FieldError("dummyRequest", "email", "E-mail inválido"));

            MethodArgumentNotValidException ex =
                    new MethodArgumentNotValidException(null, bindingResult);

            ResponseEntity<ErrorResponseDto> response = handler.handleValidation(ex);

            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

            ErrorResponseDto body = response.getBody();
            assertNotNull(body);

            assertAll(
                    () -> assertNotNull(body.getTimestamp()),
                    () -> assertTimestampProximaDeAgora(body.getTimestamp()),
                    () -> assertEquals(HttpStatus.BAD_REQUEST.value(), body.getStatus()),
                    () -> assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), body.getError()),
                    () -> assertEquals("Erro de validação nos dados informados.", body.getMessage()),
                    () -> assertNotNull(body.getFieldErrors()),
                    () -> assertEquals(2, body.getFieldErrors().size())
            );

            List<ErrorResponseDto.FieldErrorDto> fieldErrors = body.getFieldErrors();

            assertAll(
                    () -> assertEquals("nome", fieldErrors.get(0).getField()),
                    () -> assertEquals("Nome é obrigatório", fieldErrors.get(0).getMessage()),
                    () -> assertEquals("email", fieldErrors.get(1).getField()),
                    () -> assertEquals("E-mail inválido", fieldErrors.get(1).getMessage())
            );
        }

        @Test
        @DisplayName("deve retornar lista vazia quando não houver field errors")
        void deveRetornarListaVaziaQuandoNaoHouverFieldErrors() {
            DummyRequest target = new DummyRequest();
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(target, "dummyRequest");

            MethodArgumentNotValidException ex =
                    new MethodArgumentNotValidException(null, bindingResult);

            ResponseEntity<ErrorResponseDto> response = handler.handleValidation(ex);

            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

            ErrorResponseDto body = response.getBody();
            assertNotNull(body);

            assertAll(
                    () -> assertEquals("Erro de validação nos dados informados.", body.getMessage()),
                    () -> assertNotNull(body.getFieldErrors()),
                    () -> assertTrue(body.getFieldErrors().isEmpty())
            );
        }
    }

    @Nested
    @DisplayName("handleConstraintViolation")
    class HandleConstraintViolation {

        @Test
        @DisplayName("deve retornar 400 com mensagem da constraint")
        void deveRetornar400ComMensagemDaConstraint() {
            ConstraintViolationException ex =
                    new ConstraintViolationException("Parâmetro inválido.", java.util.Set.of());

            ResponseEntity<ErrorResponseDto> response = handler.handleConstraintViolation(ex);

            assertErrorResponse(
                    response,
                    HttpStatus.BAD_REQUEST,
                    "Parâmetro inválido.",
                    0
            );
        }
    }

    @Nested
    @DisplayName("handleIllegalArgument")
    class HandleIllegalArgument {

        @Test
        @DisplayName("deve retornar 400 com mensagem da illegal argument")
        void deveRetornar400ComMensagemDaIllegalArgument() {
            IllegalArgumentException ex = new IllegalArgumentException("Segmento inválido.");

            ResponseEntity<ErrorResponseDto> response = handler.handleIllegalArgument(ex);

            assertErrorResponse(
                    response,
                    HttpStatus.BAD_REQUEST,
                    "Segmento inválido.",
                    0
            );
        }
    }

    private static void assertErrorResponse(ResponseEntity<ErrorResponseDto> response,
                                            HttpStatus expectedStatus,
                                            String expectedMessage,
                                            int expectedFieldErrorsSize) {
        assertNotNull(response);
        assertEquals(expectedStatus, response.getStatusCode());

        ErrorResponseDto body = response.getBody();
        assertNotNull(body);

        assertAll(
                () -> assertNotNull(body.getTimestamp()),
                () -> assertTimestampProximaDeAgora(body.getTimestamp()),
                () -> assertEquals(expectedStatus.value(), body.getStatus()),
                () -> assertEquals(expectedStatus.getReasonPhrase(), body.getError()),
                () -> assertEquals(expectedMessage, body.getMessage()),
                () -> assertNotNull(body.getFieldErrors()),
                () -> assertEquals(expectedFieldErrorsSize, body.getFieldErrors().size())
        );
    }

    private static void assertTimestampProximaDeAgora(LocalDateTime timestamp) {
        LocalDateTime agora = LocalDateTime.now();
        assertFalse(timestamp.isBefore(agora.minusSeconds(5)));
        assertFalse(timestamp.isAfter(agora.plusSeconds(5)));
    }

    private static final class DummyRequest {
    }
}