package br.com.robertocarneiro.pautaapi.exceptions.handlers;

import br.com.robertocarneiro.pautaapi.exceptions.DefaultException;
import br.com.robertocarneiro.pautaapi.exceptions.dtos.ResponseExceptionDTO;
import br.com.robertocarneiro.pautaapi.utils.MessageUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String OPEN_KEY_MESSAGE = "{";
    private static final String CLOSE_KEY_MESSAGE = "}";

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseExceptionDTO> handleException(RuntimeException exception, ServletWebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (exception instanceof DefaultException) {
            status = ((DefaultException) exception).getHttpStatus();
        }
        ResponseExceptionDTO dto = ResponseExceptionDTO
                .builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(exception.getMessage())
                .path(request.getRequest().getRequestURL().toString())
                .build();
        return new ResponseEntity<>(dto, status);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseExceptionDTO dto = ResponseExceptionDTO
                .builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(ex
                        .getBindingResult()
                        .getAllErrors()
                        .stream()
                        .map(error -> getMessage(error))
                        .collect(Collectors.joining("\n")))
                .path(((ServletWebRequest)request).getRequest().getRequestURL().toString())
                .build();
        return new ResponseEntity<>(dto, status);
    }

    private String getMessage(ObjectError error) {
        String messageError = error.getDefaultMessage();
        if (nonNull(messageError)
                && messageError.substring(0, 1).equals(OPEN_KEY_MESSAGE)
                && messageError.substring(messageError.length()-1).equals(CLOSE_KEY_MESSAGE)) {
            messageError = messageError.replace(OPEN_KEY_MESSAGE, "");
            messageError = messageError.replace(CLOSE_KEY_MESSAGE, "");
            return MessageUtil.get(messageError);
        }
        return "Campo '" + ((FieldError) error).getField() + "' " + error.getDefaultMessage();
    }

}