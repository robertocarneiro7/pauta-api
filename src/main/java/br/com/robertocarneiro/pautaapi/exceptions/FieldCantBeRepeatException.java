package br.com.robertocarneiro.pautaapi.exceptions;

import br.com.robertocarneiro.pautaapi.utils.MessageUtil;
import org.springframework.http.HttpStatus;

public class FieldCantBeRepeatException extends DefaultException {

    public <T> FieldCantBeRepeatException(Class<T> clazz, String field, String value) {
        super(MessageUtil.get("message.error.field-cant-be-repeat", clazz.getSimpleName(), field, value),
                HttpStatus.CONFLICT);
    }
}
