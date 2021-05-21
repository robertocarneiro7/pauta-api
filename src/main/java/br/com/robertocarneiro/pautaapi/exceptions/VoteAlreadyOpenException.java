package br.com.robertocarneiro.pautaapi.exceptions;

import org.springframework.http.HttpStatus;

public class VoteAlreadyOpenException extends DefaultException {

    public <T> VoteAlreadyOpenException(Class<T> clazz, Long id) {
        super("Votação já aberta para a " + clazz.getSimpleName() + " com id=" + id, HttpStatus.BAD_REQUEST);
    }
}
