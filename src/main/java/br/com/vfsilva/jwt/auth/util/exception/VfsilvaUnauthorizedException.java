package br.com.vfsilva.jwt.util.exception;

import br.com.vfsilva.jwt.auth.messages.domain.ErrorMessage;

public class VfsilvaUnauthorizedException extends BasicException {
    public VfsilvaUnauthorizedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public static void invoke(ErrorMessage errorMessage) {
        throw new VfsilvaUnauthorizedException(errorMessage);
    }
}
