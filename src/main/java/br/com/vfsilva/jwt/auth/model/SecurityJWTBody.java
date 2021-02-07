package br.com.vfsilva.jwt.auth.model;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SecurityJWTBody {

    private String origin;
    private String login;
    private String name;
    private String lastName;
    private String email;
    private Long tokenExpirationTime;
}
