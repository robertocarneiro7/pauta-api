package br.com.robertocarneiro.pautaapi.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends DefaultException {

    public <T> NotFoundException(Class<T> clazz, Long id) {
        super(clazz.getSimpleName() + " com id=" + id + " n�o foi encontrado", HttpStatus.NOT_FOUND);

    }
}
