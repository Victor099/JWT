package br.com.vfsilva.jwt.api.util;

import br.com.vfsilva.jwt.auth.messages.domain.ErrorMessage;
import br.com.vfsilva.jwt.util.exception.VfsilvaException;
import br.com.vfsilva.jwt.util.exception.VfsilvaMethodNotFoundException;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;

/**
 * Classe utilitária controller
 *
 * @param <APP> Classe de aplicação
 * @param <P> Classe de parâmetro para realizar o login
 */
public class ControllerUtil<APP, P> {

    private final Class<APP> clazz;

    public ControllerUtil(Class<APP> clazz) {
        this.clazz = clazz;
    }

    /**
     * Método responsável por acessar o método da classe APP com o nome de "login"
     *
     * @param app Instância da classe de aplicação
     * @param param Parâmetros do método "login" da classe APP
     * @return Objeto de retorno do método "login" da classe APP
     */
    Object executeMethodLogin(APP app, P param) {
        try {
            return app.getClass().getMethod("login", param.getClass()).invoke(app, param);
        } catch (NoSuchMethodException e) {
            throw new VfsilvaMethodNotFoundException(ErrorMessage.builder()
                    .title("Atenção.")
                    .error("Não foi possível localizar o método com o nome (login).")
                    .build());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new VfsilvaException(ErrorMessage.builder()
                    .title("Atenção.")
                    .error("e.getMessage()")
                    .build());
        }
    }

    /**
     * Método responsável por acessar o método da classe APP com o nome de "refreshToken"
     *
     * @param app Instância da classe de aplicação
     * @return Objeto de retorno do método "refreshToken" da classe APP
     */
    Object executeMethodRefreshToken(APP app) {
        try {
            return app.getClass().getMethod("refreshToken").invoke(app);
        } catch (NoSuchMethodException e) {
            throw new VfsilvaMethodNotFoundException(ErrorMessage.builder()
                    .title("Atenção.")
                    .error("Não foi possível localizar o método com o nome (refreshToken) sem parâmetros.")
                    .build());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new VfsilvaException(ErrorMessage.builder()
                    .title("Atenção.")
                    .error("teste")
                    .build());
        }
    }

    /**
     * Método responsável por pegar instância da classe APP
     * @param applicationContext Responsável para pegar o Bean da classe
     * @return Instância da classe APP com bean
     */
    APP getInstanceApp(ApplicationContext applicationContext) {
        return applicationContext.getBean(clazz);
    }
}
