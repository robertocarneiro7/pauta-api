package br.com.robertocarneiro.pautaapi.exceptions;

import br.com.robertocarneiro.pautaapi.utils.MessageUtil;
import org.springframework.http.HttpStatus;

public class VoteClosedException extends DefaultException {

    public VoteClosedException(Long pautaId) {
        super(MessageUtil.get("message.error.vote-closed",pautaId), HttpStatus.BAD_REQUEST);
    }
}
