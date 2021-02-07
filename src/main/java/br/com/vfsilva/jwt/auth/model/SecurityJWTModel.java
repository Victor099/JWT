package br.com.vfsilva.jwt.auth.model;

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
