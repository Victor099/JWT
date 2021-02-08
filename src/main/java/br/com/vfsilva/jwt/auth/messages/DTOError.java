package br.com.vfsilva.jwt.auth.messages;

import br.com.vfsilva.jwt.auth.messages.domain.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DTOError {

	private ErrorMessage failure;

}
