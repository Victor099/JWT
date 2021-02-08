package br.com.vfsilva.jwt.api.util;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.InvocationTargetException;

/**
 * Classe abstrata para o controller de login e refresh token.
 * (Endpoints do @PostMapping e @PostMapping('/refreshToken') reservados)
 *
 * @param <APP> Classe app responsável para acessar o service e converter model para DTO
 *             (Classe deve ser um bean Ex.: @Service ou @Component)
 * @param <P> Classe de parâmetro para realizar o login
 */
@Slf4j
public abstract class AbstractLoginController<APP, P> extends ControllerUtil<APP, P> {

    @Autowired
    public APP app;

    @PostMapping
    public ResponseEntity<Object> login(@RequestBody final P param) {
        return ResponseEntity.ok(executeMethodLogin(app, param));
    }

    @PostMapping("/refreshToken")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "Authorization", required = true, paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", required = true, paramType = "header")}
    )
    public ResponseEntity<Object> refreshToken() {
        return ResponseEntity.ok(executeMethodRefreshToken(app));
    }
}

