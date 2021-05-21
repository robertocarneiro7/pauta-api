package br.com.robertocarneiro.pautaapi.exceptions;

import br.com.robertocarneiro.pautaapi.utils.MessageUtil;
import org.springframework.http.HttpStatus;

public class AlreadyVotedException extends DefaultException {

    public <T> AlreadyVotedException(Class<T> clazz, Long associadoId, Long pautaId) {
        super(MessageUtil.get("message.error.already-voted",clazz.getSimpleName(),associadoId,pautaId),
                HttpStatus.BAD_REQUEST);
    }
}
