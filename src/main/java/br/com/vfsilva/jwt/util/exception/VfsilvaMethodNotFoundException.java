package br.com.vfsilva.jwt.util.exception;

import br.com.vfsilva.jwt.auth.messages.domain.ErrorMessage;

public class VfsilvaMethodNotFoundException extends BasicException {

    public VfsilvaMethodNotFoundException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
