package br.com.robertocarneiro.pautaapi.exceptions;

import br.com.robertocarneiro.pautaapi.utils.MessageUtil;
import org.springframework.http.HttpStatus;

public class VoteAlreadyOpenException extends DefaultException {

    public <T> VoteAlreadyOpenException(Class<T> clazz, Long id) {
        super(MessageUtil.get("message.error.vote-already-open",clazz.getSimpleName(),id),
                HttpStatus.BAD_REQUEST);
    }
}
