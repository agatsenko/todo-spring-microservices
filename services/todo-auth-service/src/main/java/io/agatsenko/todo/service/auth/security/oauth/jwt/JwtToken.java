/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-19
 */
package io.agatsenko.todo.service.auth.security.oauth.jwt;

import java.util.Optional;

public interface JwtToken {
    static JwtTokenBuilder builder() {
        return new JwtTokenBuilder();
    }

    JwtHeader getHeader();

    JwtPayload getPayload();

    Optional<String> getSignature();

    String getTokenValue();

    default boolean isExpired() {
        return getPayload().getExp().map(exp -> (exp.toEpochMilli() - System.currentTimeMillis()) <= 0).orElse(false);
    }
}
