package br.com.security.jwt.messages.domain;

import br.com.security.jwt.messages.TypeMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessage implements Serializable {

	private static final long serialVersionUID = 3425074067837552028L;

	private String title;
	private String error;
	private List<ErrorMessage> details;

}
