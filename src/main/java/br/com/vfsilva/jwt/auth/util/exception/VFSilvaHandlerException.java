package br.com.vfsilva.jwt.auth.util.exception;

import br.com.vfsilvacore.util.message.DTOError;
import br.com.vfsilvacore.util.message.ErrorMessage;
import br.com.vfsilvacore.util.message.stack.ErrorStack;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Classe default Handler Exception das aplicações web.
 *
 * @author Victor Felix da Silva (vfsilva099@gmail.com)
 */
@ControllerAdvice
@RequiredArgsConstructor
public class VFSilvaHandlerException extends ResponseEntityExceptionHandler {

    private final ErrorStack errorStack;

    @ExceptionHandler(VfsilvaException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handlerMethodNotFoundException(final VfsilvaException ex) {
        return new ResponseEntity<>(new DTOError(ex.getErrorMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(VfsilvaMethodNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handlerMethodNotFoundException(final VfsilvaMethodNotFoundException ex) {
        return new ResponseEntity<>(new DTOError(ex.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VFSilvaExpirationTypeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handlerExpirationTypeException(final VFSilvaExpirationTypeException ex) {
        return new ResponseEntity<>(new DTOError(ex.getErrorMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(VfsilvaUnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> handlerExpirationTypeException(final VfsilvaUnauthorizedException ex) {
        return new ResponseEntity<>(new DTOError(ex.getErrorMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleGenericException(final Exception ex) {

        errorStack.addMessage(ex.getMessage());

        final ErrorMessage error = ErrorMessage
                .builder()
                .title("Atenção")
                .error("Falha na aplicação")
                .details(errorStack.getErrors())
                .build();

        return new ResponseEntity<>(new DTOError(error), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
