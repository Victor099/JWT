package br.com.security.jwt.controller;

import br.com.security.jwt.service.ILoginService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController(value = "/login")
public class BaseLoginController<P, R> {

    private final ILoginService<P, R> service;

    @PostMapping
    public ResponseEntity<R> login(final P parameter) {
        return ResponseEntity.ok(service.login(parameter));
    }
}
