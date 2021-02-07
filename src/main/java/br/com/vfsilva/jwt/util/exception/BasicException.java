package br.com.vfsilva.jwt.util.exception;

import br.com.vfsilva.jwt.auth.messages.domain.ErrorMessage;
import lombok.ToString;

import java.text.MessageFormat;

/**
 * Classe básica de exceção da aplicação.
 *
 * @author Victor Felix da Silva (vfsilva099@gmail.com)
 */
@ToString
public class BasicException extends RuntimeException {

	private static final long serialVersionUID = -7198616026085428301L;

	private ErrorMessage error;

	/**
	 * Contructor da Exceção.
	 *
	 * @param error
	 */
	public BasicException(ErrorMessage error) {
		super(MessageFormat.format("{0}: {1} ({2})", error.getTitle(), error.getError(), error.getDetails()));
		this.error = error;
	}
}
