package br.com.robertocarneiro.pautaapi.exceptions;

import org.springframework.http.HttpStatus;

public class FieldCantBeRepeatException extends DefaultException {

    public <T> FieldCantBeRepeatException(Class<T> clazz, String field, String value) {
        super(clazz.getSimpleName() + " com campo '" + field + "' com valor='" + value + "' não pode ser repetido no banco",
                HttpStatus.CONFLICT);
    }
}
