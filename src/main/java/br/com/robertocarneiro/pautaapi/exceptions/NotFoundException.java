package br.com.robertocarneiro.pautaapi.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class NotFoundException extends RuntimeException {

    public <T> NotFoundException(Class<T> clazz, Long id) {
        super(clazz.getSimpleName() + " com id=" + id + " não foi encontrado");
    }
}
