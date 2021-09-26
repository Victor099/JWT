package br.com.vfsilva.jwt.auth.util.exception;

import br.com.vfsilvacore.util.message.ErrorMessage;

public class VfsilvaException extends BasicException {
    public VfsilvaException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public static void invoke(ErrorMessage errorMessage) {
        throw new VfsilvaException(errorMessage);
    }
}
