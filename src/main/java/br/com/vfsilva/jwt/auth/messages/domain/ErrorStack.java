package br.com.vfsilva.jwt.auth.messages.domain;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.List;

@RequestScope
@Component
public class ErrorStack {

    private List<ErrorMessage> errors;

    public ErrorStack() {
        this.errors = new ArrayList<>();
    }

    public void addMessage(final String error) {
        final ErrorMessage handler = ErrorMessage.builder().error(error).build();
        this.errors.add(handler);
    }

    public List<ErrorMessage> getErrors() {
        return this.errors;
    }

    public Boolean isError() {
        return this.errors.size() > 0;
    }
}
