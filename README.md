# Security JWT
Dependência feita para quem for implementar não precisar se preocupar
em criar o endpoint de login e refreshToken.

# Como usar:
### Controller:
Extends o AbstractLoginController<APP, P>
- APP: Classe de APP (classe que chama o service e converte a sua resposta para o controller 
  Obs.: classe deve ser um @Component, @Service…).
- P: Classe do body para requisição de login.

### App:
Implements IVFSilvaLoginApp<RETURN, PARAM> que contém os métodos de login e refreshToken obrigando assim a implementar os métodos.
- RETURN: Classe para retornar da classe APP para o controller.
- PARAM: Classe para passar como parâmetro para o service.

# Exemplos: 
### Controller

```
@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/auth/login")
@Api(value = "[ /auth/login ] - API Login", tags = {"Login"})
public class Main extends AbstractLoginController<LoginApp, LoginInputDTO> {

}
```
### App:
```
@AllArgsConstructor
@Service
public class LoginApp implements IVFSilvaLoginApp<LoginDTO, LoginInputDTO> {

    private final ILoginService service;

    @Override
    public LoginDTO login(LoginInputDTO loginInputDTO) {
        return service.login(loginInputDTO);
    }

    @Override
    public LoginDTO refreshToken() {
        return service.refreshToken();
    }
}
```
