package br.com.denram.empreendimentos_sc_api.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
