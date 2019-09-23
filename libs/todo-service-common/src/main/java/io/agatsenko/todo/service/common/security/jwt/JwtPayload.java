/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-19
 */
package io.agatsenko.todo.service.common.security.jwt;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface JwtPayload {
    String JTI_KEY = "jti";
    String EXP_KEY = "exp";
    String TOKEN_TYPE_KEY = "token_type";
    String CLIENT_ID_KEY = "client_id";
    String USER_ID_KEY = "user_id";
    String USERNAME_KEY = "username";
    String SCOPE_KEY = "scope";
    String AUTHORITIES_KEY = "authorities";

    static JwtPayloadBuilder builder() {
        return new JwtPayloadBuilder();
    }

    Optional<UUID> getJti();

    Optional<Instant> getExp();

    Optional<JwtTokenType> getTokenType();

    Optional<String> getClientId();

    Optional<UUID> getUserId();

    Optional<String> getUsername();

    Set<String> getScope();

    Set<String> getAuthorities();

    <T> Optional<T> get(String key, Class<T> valueClass);

    Map<String, Object> toMap();
}
