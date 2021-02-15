package br.com.vfsilva.jwt.api.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Classe abstrata para o controller de login e refresh token.
 * (Endpoints do @PostMapping e @PostMapping('/refreshToken') reservados)
 *
 * @param <APP> Classe app responsável para acessar o service e converter model para DTO
 *             (Classe deve ser um bean Ex.: @Service ou @Component)
 * @param <P> Classe de parâmetro para realizar o login
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractLoginController<APP, P> extends ControllerUtil<APP, P> {

    protected final APP app;

    @PostMapping
    public ResponseEntity<Object> login(@RequestBody final P param) {
        return ResponseEntity.ok(executeMethodLogin(app, param));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<Object> refreshToken() {
        return ResponseEntity.ok(executeMethodRefreshToken(app));
    }
}

