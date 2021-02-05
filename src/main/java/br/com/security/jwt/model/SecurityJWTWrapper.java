package br.com.security.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class SecurityJWTWrapper {

    private String login;
    private String name;
    private String lastName;
    private String email;
    private Long tokenExpirationTime;

}
