package br.com.denram.empreendimentos_sc_api.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorResponseDto {

    LocalDateTime timestamp;
    int status;
    String error;
    String message;
    List<FieldErrorDto> fieldErrors;

    @Value
    @Builder
    public static class FieldErrorDto {
        String field;
        String message;
    }
}