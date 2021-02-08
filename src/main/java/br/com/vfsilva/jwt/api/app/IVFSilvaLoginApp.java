package br.com.vfsilva.jwt.api.app;

/**
 * Interface a ser implementada na camada app
 * (Classe responsável pela conversão de model retornada pelo service para DTO que o controller espera)
 *
 * @param <RETURN> Tipo da classe que retornará para o controller
 * @param <PARAM> Tipo da classe que o service espera por parâmetro
 */
public interface IVFSilvaLoginApp<RETURN, PARAM> {

    RETURN login(PARAM param);

    RETURN refreshToken();
}
