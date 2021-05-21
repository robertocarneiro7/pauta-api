package br.com.robertocarneiro.pautaapi.exceptions;

import br.com.robertocarneiro.pautaapi.utils.MessageUtil;
import org.springframework.http.HttpStatus;

public class ExternalServiceDidNotAllowVoteException extends DefaultException {

    public ExternalServiceDidNotAllowVoteException(String cpf) {
        super(MessageUtil.get("message.error.external-service-did-not-allow-vote", cpf), HttpStatus.BAD_REQUEST);
    }
}
