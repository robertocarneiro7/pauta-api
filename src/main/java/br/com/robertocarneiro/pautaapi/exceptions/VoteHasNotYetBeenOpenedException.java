package br.com.robertocarneiro.pautaapi.exceptions;

import br.com.robertocarneiro.pautaapi.utils.MessageUtil;
import org.springframework.http.HttpStatus;

public class VoteHasNotYetBeenOpenedException extends DefaultException {

    public VoteHasNotYetBeenOpenedException(Long pautaId) {
        super(MessageUtil.get("message.error.vote-has-not-yet-been-opened",pautaId), HttpStatus.BAD_REQUEST);
    }
}
