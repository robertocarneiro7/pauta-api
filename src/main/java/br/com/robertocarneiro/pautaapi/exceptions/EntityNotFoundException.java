package br.com.robertocarneiro.pautaapi.exceptions;

import br.com.robertocarneiro.pautaapi.utils.MessageUtil;
import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends DefaultException {

    public <T> EntityNotFoundException(Class<T> clazz, Long id) {
        super(MessageUtil.get("message.error.entity-not-found", clazz.getSimpleName(), id), HttpStatus.NOT_FOUND);

    }
}
