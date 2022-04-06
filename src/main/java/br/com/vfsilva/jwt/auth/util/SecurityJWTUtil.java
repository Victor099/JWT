package br.com.vfsilva.jwt.auth.util;

import br.com.vfsilva.jwt.auth.messages.domain.ErrorStack;
import br.com.vfsilva.jwt.auth.model.SecurityJWTBody;
import br.com.vfsilva.jwt.auth.model.SecurityJWTModel;
import br.com.vfsilva.jwt.auth.model.SecurityJWTWrapper;
import com.google.gson.Gson;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

import static br.com.vfsilva.jwt.auth.util.SecurityJWTStatus.*;
import static java.util.Objects.isNull;

/**
 * Classe utilitária responsável por efetuar as requisições jwt.
 *
 * @author Victor Felix da Silva (vfsilva099@gmail.com)
 */
@Component
public class SecurityJWTUtil {

    @Autowired
    private Environment env;

    private final String SECRET_JWT = "536186bbff1dc3c493540906147774da3f20c830a18a7e205ed4a31d053a8195";
    private final String TOKEN_PREFIX = "Bearer ";
    private final String HEADER_AUTHORIZATION = "Authorization";
    private final String HEADER_REFRESH_TOKEN = "RefreshToken";

    /**
     * Método responsável pela criação do token e autenticação jwt.
     *
     * @param request  Http request
     * @param response Http response
     * @param handler  Corpo do que vai estar dentro do authorization
     */
    public void addAuthentication(final HttpServletRequest request, final HttpServletResponse response, final SecurityJWTWrapper handler) {
        final String host = capturaOrigemRequest(request);
        SecurityJWTModel token = generateToken(handler, host);

        response.addHeader(HEADER_AUTHORIZATION, token.getToken());
        response.addHeader(HEADER_REFRESH_TOKEN, token.getRefreshToken());
    }

    /**
     * Validação do Token JWT da requisição.
     *
     * @param request    Resquest {@link HttpServletRequest}
     * @param errorStack Stack de erro {@link ErrorStack}
     */
    public SecurityJWTStatus validateAuthentication(final HttpServletRequest request, final ErrorStack errorStack) {

        final var token = request.getHeader(HEADER_AUTHORIZATION);

        if (isNull(token)) {
            errorStack.addMessage("sessao_nula");
            return NULL;
        }

        // RETIRANDO BEARER DO TOKEN
        final var jwt = token.split(TOKEN_PREFIX);

        if (jwt.length < 2) {
            errorStack.addMessage("sessao_invalida");
            return INVALID;
        }

        Jws<Claims> claims = null;

        try {

            claims = decodeJWT(jwt[1]);

            final var bodyJWT = new Gson().fromJson(claims.getBody().getIssuer(), SecurityJWTBody.class);

        } catch (ExpiredJwtException e) {
            return validateTypeTokenExpiration(jwt[1], errorStack);

        } catch (Exception e) {
            errorStack.addMessage("sessao_invalida");
            return INVALID;
        }

        return OK;
    }

    /**
     * Método responsável pela verificação para realizar o refreshToken
     *
     * @param request    Http request
     * @param response   Http response
     * @param errorStack Pilha de erros
     * @return Status para realizar o RefreshToken
     */
    public SecurityJWTStatus validateRefreshToken(final HttpServletRequest request,
                                                  final HttpServletResponse response,
                                                  final ErrorStack errorStack) {

        final String token = request.getHeader(HEADER_AUTHORIZATION);
        final String refreshToken = request.getHeader(HEADER_REFRESH_TOKEN);

        if (isNull(token)) {
            errorStack.addMessage("Sessão nula.");
            return INVALID;
        }

        // RETIRANDO BEARER DO TOKEN
        String[] _jwt = token.split(TOKEN_PREFIX);

        if (_jwt.length < 2) {
            errorStack.addMessage("Sessão inválida.");
            return INVALID;
        }

        Jws<Claims> claims = null;

        try {
            claims = decodeToleranceJWT(_jwt[1]);

            String login = claims.getBody().getSubject();

            String clientID = claims.getBody().getId();

            String _refreshToken = CryptoUtil.build().decrypt(clientID, refreshToken);
            SecurityRefreshToken refreshTokenModel = new Gson().fromJson(_refreshToken, SecurityRefreshToken.class);

            if (!login.equals(refreshTokenModel.getLogin())) {
                errorStack.addMessage("Sessão inválida.");
                return INVALID;
            }

        } catch (ExpiredJwtException e) {
            errorStack.addMessage("Tempo de sessão expirado.");
            return INVALID;
        } catch (Exception e) {
            errorStack.addMessage("Sessão inválida.");
            return INVALID;
        }

        return OK;
    }

    public SecurityJWTBody getBodyJWT(final HttpServletRequest request, final ErrorStack errorStack) {
        final String token = request.getHeader(HEADER_AUTHORIZATION);
        String[] _jwt = token.split(TOKEN_PREFIX);

        if (_jwt.length < 2) {
            errorStack.addMessage("Sessão inválida.");
        }

        Jws<Claims> claims = decodeToleranceJWT(_jwt[1]);

        if (isNull(claims.getBody())) {
            errorStack.addMessage("Erro ao buscar o corpo do authentication");
            return null;
        }

        return new Gson().fromJson(claims.getBody().getIssuer(), SecurityJWTBody.class);
    }

    /**
     * Método responsável por gerar o token e o refreshToken
     *
     * @param handler Pacote do que ficará encapsulado dentro do token
     * @param host    Origem de acesso para gerar o token
     * @return Token e RefreshToken
     */
    public SecurityJWTModel generateToken(final SecurityJWTWrapper handler, final String host) {

        String clientID = SecurityUUID.generateID();

        // BODY TOKEN JWT
        SecurityJWTBody body = SecurityJWTBody
                .builder()
                .origin(host)
                .name(handler.getName())
                .email(handler.getEmail())
                .login(handler.getLogin())
                .lastName(handler.getLastName())
                .build();

        // CRIAÇÃO REFRESH TOKEN
        SecurityRefreshToken _refreshToken = SecurityRefreshToken
                .builder()
                .login(handler.getLogin())
                .email(handler.getEmail())
                .build();

        String refreshToken = CryptoUtil.build().crypt(clientID, new Gson().toJson(_refreshToken));

        // TEMPO DE EXPIRAÇÃO DAS REQUISIÇÕES.
        Date dataExpiracao;
        Date dataDeCriacao = new Date();

        Long expirationTimeMillis = isNull(handler.getTokenExpirationTime())
                ? getConfigurationExpirationTime()
                : getConfigurationExpirationTime(handler.getTokenExpirationTime());
        dataExpiracao = new Date(dataDeCriacao.getTime() + expirationTimeMillis);

        // CRIAÇÃO DO JWT
        String tokenJWT = Jwts
                .builder()
                .setId(clientID)
                .setIssuedAt(dataDeCriacao)
                .setSubject(handler.getLogin())
                .setIssuer(new Gson().toJson(body))
                .setExpiration(dataExpiracao)
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(SECRET_JWT.getBytes()))
                .compact();

        return SecurityJWTModel.builder().token(TOKEN_PREFIX + tokenJWT).refreshToken(refreshToken).build();
    }

    /**
     * Método responsável por decodificar o authorization para conseguir pegar as informações
     * @param token Authorization
     * @return Authorization decodificado
     */
    private Jws<Claims> decodeToleranceJWT(String token) {

        return Jwts.parser()
                .setAllowedClockSkewSeconds(getConfigurationToleranceTime())
                .setSigningKey(Base64.getEncoder().encodeToString(SECRET_JWT.getBytes()))
                .parseClaimsJws(token);
    }

    private Long getConfigurationToleranceTime() {
        long TEMPO_DE_TOLERANCIA = env.getRequiredProperty("jwt.tolerance.minutes", Long.class);

        if (TEMPO_DE_TOLERANCIA < 0) {
            // 5 MINUTOS.
            return (long) 300;
        }
        // RETORNO EM SEGUNDOS
        return TEMPO_DE_TOLERANCIA * 60;

    }

    /**
     * Método responsável pela captura de origem de acesso.
     *
     * @param request Http request
     * @return origem de acesso
     */
    private String capturaOrigemRequest(HttpServletRequest request) {
        return (request.getRemoteAddr() != null && !"".equals(request.getRemoteAddr())) ? request.getRemoteAddr() : request.getHeader("Origin");
    }

    /**
     * Método responsável de devolver o tempo de expiração do token.
     *
     * @return Tempo de expiração.
     */
    private Long getConfigurationExpirationTime() {

        long TEMPO_EXPIRACAO = env.getRequiredProperty("jwt.expiration.minutes", Long.class);


        if (TEMPO_EXPIRACAO <= 0) {
            // 20 MINUTOS - TEMPO PADRÃO.
            return (long) 1200000;
        }

        return getConfigurationExpirationTime(TEMPO_EXPIRACAO);
    }

    /**
     * Método responsável por transformar o tempo em milissegundos
     * @param tokenExpirationTime Long
     * @return Tempo de expiração em milissegundos
     */
    private Long getConfigurationExpirationTime(Long tokenExpirationTime) {
        //RETORNO EM MILISSEGUNDOS
        return tokenExpirationTime * 60000;
    }

    /**
     * Decodificação JWT (sem tolerância).
     *
     * @param token Token {@link String}
     */
    private Jws<Claims> decodeJWT(final String token) {

        return Jwts.parser()
                .setSigningKey(SECRET_JWT)
                .parseClaimsJws(token);
//                .setSigningKey(Base64.getEncoder().encodeToString(properties.getSecretJWT().getBytes()))
    }

    /**
     * Validação de tolerancia de Token.
     */
    private SecurityJWTStatus validateTypeTokenExpiration(final String token, final ErrorStack errorStack) {

        try {
            decodeToleranceJWT(token);
            errorStack.addMessage("tempo_sessao_expirou");
            return TIMEOUT;

        } catch (Exception e) {
            errorStack.addMessage("sessao_invalida");
            return INVALID;
        }
    }

}
