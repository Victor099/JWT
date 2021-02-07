package br.com.vfsilva.jwt.api.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
public abstract class AbstractLoginController<APP, P> extends ControllerUtil<APP, P> {

    @Autowired
    private ApplicationContext applicationContext;

    public AbstractLoginController(Class<APP> clazz) {
        super(clazz);
    }

    @PostMapping
    public ResponseEntity<Object> login(@RequestBody final P param) {
        APP app = getInstanceApp(applicationContext);
        return ResponseEntity.ok(executeMethodLogin(app, param));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<Object> refreshToken() {
        APP app = getInstanceApp(applicationContext);
        return ResponseEntity.ok(executeMethodRefreshToken(app));
    }
}

