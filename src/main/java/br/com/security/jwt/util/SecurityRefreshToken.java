package br.com.security.jwt.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class SecurityRefreshToken {

    private String login;
    private String email;
}
