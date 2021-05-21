package br.com.robertocarneiro.pautaapi.exceptions;

import br.com.robertocarneiro.pautaapi.utils.MessageUtil;
import org.springframework.http.HttpStatus;

public class InvalidVoteOptionException extends DefaultException {

    public InvalidVoteOptionException() {
        super(MessageUtil.get("message.error.invalid-vote-option"), HttpStatus.BAD_REQUEST);
    }
}
