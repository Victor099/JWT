package br.com.security.jwt.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SecurityJWTModel {

    private String token;
    private String refreshToken;
}
