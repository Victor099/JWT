package br.com.vfsilva.jwt.auth.messages.domain;

import br.com.vfsilva.jwt.auth.messages.TypeMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message implements Serializable {

	private static final long serialVersionUID = 3425074067837552028L;

	private TypeMessage type;
	private String title;
	private String description;

}
