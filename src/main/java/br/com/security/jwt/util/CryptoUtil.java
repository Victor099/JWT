package br.com.security.jwt.util;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.util.text.BasicTextEncryptor;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * Classe responsável por realizar criptografia de senha.
 *
 * @author Victor Felix da Silva (vfsilva099@gmail.com)
 */
@Log4j2
public class CryptoUtil {

    private BasicTextEncryptor bte;

    private CryptoUtil() {
        bte = new BasicTextEncryptor();
    }

    /**
     * Utilitário para instanciação dos Algoritmos de criptografia.
     *
     * @return this.
     */
    public static CryptoUtil build() {
        return new CryptoUtil();
    }

    /**
     * Criptografia de Mensagens.
     */
    public String crypt(String key, String value) {

        bte.setPassword(key);

        return bte.encrypt(value);
    }

    /**
     * Decriptografia de Mensagens.
     */
    public String decrypt(String key, String value) {

        bte.setPassword(key);

        return bte.decrypt(value);

    }

    /**
     * Geração da criptografia da senha do usuário.
     */
    public String generateCriptPassword(String handler) {

        log.debug("CryptoUtil.generateCriptPassword");

        if (StringUtils.isNotBlank(handler)) {

            // GENERATE BASE64.
            String _hash = encodeBase64(handler);

            try {

                MessageDigest m = MessageDigest.getInstance("SHA-512");
                m.update(_hash.getBytes(), 0, _hash.length());

                String crypt = new BigInteger(1, m.digest()).toString(16);

                // ARMAZENANDO CRYPT REVERSO.
                return reverseString(crypt);

            } catch (Exception ex) {
                log.error("FALHA AO GERAR CRIPTOGRAFIA DE SENHA: {}", ex.getMessage());
                return null;
            }
        }

        return null;

    }

    /**
     * Transformando String em Base64.
     */
    private String encodeBase64(String handler) {

        return new String(Base64.getEncoder().encodeToString(handler.getBytes()));
    }

    /**
     * Revertendo String.
     */
    private String reverseString(String handler) {

        return new StringBuilder(handler).reverse().toString();
    }
}
