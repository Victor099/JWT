# Security JWT
Dependência feita para quem for implementar não precisar se preocupar
em criar o endpoint de login e refreshToken utilizando o Java Security JWT.

- [JWT](https://jwt.io/)

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

### Service:
Nessa camada é onde será implementado a lógica de geração de token.

# Exemplos: 
### Controller

```
@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/auth/login")
@Api(value = "[ /auth/login ] - API Login", tags = {"Login"})
public class LoginController extends AbstractLoginController<LoginApp, LoginInputDTO> {

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
### Service:
```
@AllArgsConstructor
@Service
public class LoginService extends ServiceUtil implements ILoginService {

    private final ErrorStack errorStack;
    private final HttpServletResponse response;
    private final HttpServletRequest request;
    private final SecurityJWTUtil jwtUtil;

    @Override
    public LoginDTO login(final LoginInputDTO param) {
        validateLogin(param);

        if (errorStack.isError()) {
            VfsilvaException.invoke(generateError("Erro ao efetuar o login.", errorStack.getErrors()));
        }

        SecurityJWTWrapper wrapper = SecurityJWTWrapper.builder()
                .login(param.getUsername())
                .name("Victor")
                .email("vfsilva099@gmail.com")
                .lastName("Felix")
                .build();

        jwtUtil.addAuthentication(request, response, wrapper);
        return LoginDTO.builder().login("vfsilva").password("minha senha").build();
    }

    @Override
    public LoginDTO refreshToken() {
        final SecurityJWTStatus statusToken = jwtUtil.validateRefreshToken(request, response, errorStack);

        if (!statusToken.equals(SecurityJWTStatus.OK)) {
            VFSilvaExpirationTypeException.invoke(generateError("Erro ao atualizar token.", errorStack.getErrors()));
        }

        final SecurityJWTBody body = jwtUtil.getBodyJWT(request, errorStack);

        if (errorStack.isError()) {
            VfsilvaException.invoke(generateError("Corpo do authentication.", errorStack.getErrors()));
        }

        final SecurityJWTWrapper wrapper = SecurityJWTWrapper.builder()
                .lastName(body.getLastName())
                .email(body.getEmail())
                .name(body.getName())
                .build();

        jwtUtil.addAuthentication(request, response, wrapper);

        return LoginDTO.builder()
                .login(body.getLogin())
                .build();
    }

    private void validateLogin(final LoginInputDTO param) {
        if (isBlank(param.getUsername())) {
            errorStack.addMessage("Login nulo.");
        }
        if (isBlank(param.getPassword())) {
            errorStack.addMessage("Senha nula.");
        }
    }
}
```

### Outras classes utilizadas:
- **ServiceUtil**: Classe utilitária responsável por criar o método ***generateError()***.
- **ILoginService**: Interface responsável por criar o contrato de todos os métodos que dever estar na camada de service.
- **ErrorStack**: É um @RequestScope o que significa que ele é um bean que será criado somente quando houver uma solicitação,
  enquanto a solicitação não for finalizada será utilizado a mesma instância dessa classe, contendo um ***addMessage(String msgError)***. 
- **MessageStack**: É um @RequestScope o que significa que ele é um bean que será criado somente quando houver uma solicitação,
  enquanto a solicitação não for finalizada será utilizado a mesma instância dessa classe, contendo um ***addMessage(TypeMessage type, String msgError)***.
- **TypeMessage**: Enumerador com tipos de mensagens (Error, Info, Warning, Success)
- **Exceptions**:
  - ***VfsilvaException***
  - ***VfsilvaUnauthorizedException***
  - ***VfsilvaExpirationTypeException***
  - ***VfsilvaMethodNotFoundException***
