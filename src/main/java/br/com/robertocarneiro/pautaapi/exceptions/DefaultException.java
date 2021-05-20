package br.com.robertocarneiro.pautaapi.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static java.util.Objects.nonNull;

@Getter
public class DefaultException extends RuntimeException {

    private final HttpStatus httpStatus;

    public DefaultException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = nonNull(httpStatus) ? httpStatus : HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
