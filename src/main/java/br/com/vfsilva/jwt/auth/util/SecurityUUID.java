package br.com.vfsilva.jwt.auth.util;

import java.util.UUID;

/**
 * Classe responsável por gerar o client-id.
 *
 * @author Victor Felix da Silva (vfsilva099@gmail.com)
 */
public class SecurityUUID {

    public static String generateID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
