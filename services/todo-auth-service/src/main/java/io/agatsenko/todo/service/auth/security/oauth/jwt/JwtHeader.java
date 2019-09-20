/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-19
 */
package io.agatsenko.todo.service.auth.security.oauth.jwt;

import java.util.Map;
import java.util.Optional;

public interface JwtHeader {
    String TYP_KEY = "typ";
    String TYP_JWT_VALUE = "JWT";
    String ALG_KEY = "alg";

    static JwtHeaderBuilder builder() {
        return new JwtHeaderBuilder();
    }

    Optional<String> getTyp();

    Optional<String> getAlg();

    <T> Optional<T> get(String key, Class<T> valueClass);

    Map<String, Object> toMap();
}
