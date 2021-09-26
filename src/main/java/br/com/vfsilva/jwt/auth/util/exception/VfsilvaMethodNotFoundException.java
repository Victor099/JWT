package br.com.vfsilva.jwt.auth.util.exception;

import br.com.vfsilvacore.util.message.ErrorMessage;

public class VfsilvaMethodNotFoundException extends BasicException {

    public VfsilvaMethodNotFoundException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
