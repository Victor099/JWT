package br.com.vfsilva.jwt.auth.util.exception;

import br.com.vfsilva.jwt.auth.messages.domain.ErrorMessage;

public class VFSilvaExpirationTypeException extends BasicException{

    public VFSilvaExpirationTypeException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public static void invoke(ErrorMessage errorMessage) {
        throw new VfsilvaException(errorMessage);
    }
}
