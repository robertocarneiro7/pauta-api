package br.com.robertocarneiro.pautaapi.exceptions;

import org.springframework.http.HttpStatus;

public class HeaderNotNullException extends DefaultException {

    public HeaderNotNullException(String key) {
        super("Header '" + key + "' é obrigatório", HttpStatus.BAD_REQUEST);
    }
}
