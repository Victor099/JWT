package br.com.vfsilva.jwt.auth.messages.domain;

import br.com.vfsilva.jwt.auth.messages.TypeMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.List;

@RequestScope
@Component
public class MessageStack {

    private final List<Message> messages;

    public MessageStack() {
        this.messages = new ArrayList<>();
    }

    public void addMessage(final TypeMessage type, final String title) {
        final Message message = Message.builder()
                .title(title)
                .type(type)
                .build();
        this.messages.add(message);
    }

    public void addMessage(final TypeMessage type, final String title, final String description) {
        final Message message = Message.builder()
                .title(title)
                .description(description)
                .type(type)
                .build();
        this.messages.add(message);
    }
}
