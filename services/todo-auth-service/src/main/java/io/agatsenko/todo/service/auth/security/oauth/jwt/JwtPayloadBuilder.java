/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-19
 */
package io.agatsenko.todo.service.auth.security.oauth.jwt;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import com.google.common.base.Strings;

import io.agatsenko.todo.util.Check;

import static io.agatsenko.todo.service.auth.security.oauth.jwt.JwtPayload.*;

public class JwtPayloadBuilder {
    private final Map<String, Object> values = new HashMap<>();

    public JwtPayloadBuilder jti(UUID value) {
        return put(JTI_KEY, value);
    }

    public Optional<UUID> getJti() {
        return get(JTI_KEY, UUID.class);
    }

    public JwtPayloadBuilder exp(Instant value) {
        return put(EXP_KEY, value);
    }

    public Optional<Instant> getExp() {
        return get(EXP_KEY, Instant.class);
    }

    public JwtPayloadBuilder tokenType(JwtTokenType value) {
        return put(TOKEN_TYPE_KEY, value);
    }

    public Optional<JwtTokenType> getTokenType() {
        return get(TOKEN_TYPE_KEY, JwtTokenType.class);
    }

    public JwtPayloadBuilder clientId(String value) {
        return put(CLIENT_ID_KEY, value);
    }

    public Optional<String> getClientId() {
        return get(CLIENT_ID_KEY, String.class);
    }

    public JwtPayloadBuilder userId(UUID value) {
        return put(USER_ID_KEY, value);
    }

    public Optional<UUID> getUserId() {
        return get(USER_ID_KEY, UUID.class);
    }

    public JwtPayloadBuilder username(String value) {
        return put(USERNAME_KEY, value);
    }

    public Optional<String> getUsername() {
        return get(USERNAME_KEY, String.class);
    }

    public JwtPayloadBuilder scope(Collection<String> value) {
        return put(
                SCOPE_KEY,
                value == null ?
                        null :
                        value.stream().filter(v -> !Strings.isNullOrEmpty(v)).collect(Collectors.toUnmodifiableSet())
        );
    }

    public JwtPayloadBuilder scope(String value) {
        if (Strings.isNullOrEmpty(value)) {
            return this;
        }
        final var set = new HashSet<>(getScope());
        set.add(value);
        return scope(set);
    }

    @SuppressWarnings("unchecked")
    public Set<String> getScope() {
        return get(SCOPE_KEY, Set.class).orElse(Collections.emptySet());
    }

    public JwtPayloadBuilder authorities(Collection<String> value) {
        return put(
                AUTHORITIES_KEY,
                value == null ?
                        null :
                        value.stream().filter(v -> !Strings.isNullOrEmpty(v)).collect(Collectors.toUnmodifiableSet())
        );
    }

    public JwtPayloadBuilder grantedAuthorities(Collection<? extends GrantedAuthority> value) {
        return put(
                AUTHORITIES_KEY,
                value == null ?
                        null :
                        value.stream()
                                .filter(Objects::nonNull)
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toUnmodifiableSet())
        );
    }

    public JwtPayloadBuilder authority(String value) {
        if (Strings.isNullOrEmpty(value)) {
            return this;
        }
        final var set = new HashSet<>(getAuthorities());
        set.add(value);
        return authorities(set);
    }

    public JwtPayloadBuilder authority(GrantedAuthority authority) {
        if (authority == null) {
            return this;
        }
        return authority(authority.getAuthority());
    }

    @SuppressWarnings("unchecked")
    public Set<String> getAuthorities() {
        return get(AUTHORITIES_KEY, Set.class).orElse(Collections.emptySet());
    }

    public JwtPayloadBuilder put(String key, Object value) {
        validate(key, value);
        if (value == null || (value instanceof String && ((String) value).isEmpty())) {
            values.remove(key);
        }
        else {
            values.put(key, value);
        }
        return this;
    }

    public <T> Optional<T> get(String key, Class<T> valueClass) {
        return Optional.ofNullable(valueClass.cast(values.get(key)));
    }

    public JwtPayloadBuilder clear() {
        values.clear();
        return this;
    }

    public JwtPayload build() {
        return new JwtPayloadImpl(values);
    }

    private void validate(Object value, Class<?> expectedClass) {
        if (value == null) {
            return;
        }
        Check.arg(expectedClass.isInstance(value), "expected that %s value to be %s", value, expectedClass.getName());
    }

    private void validate(String key, Object value) {
        Check.argNotEmpty(key, "key");
        if (value == null) {
            return;
        }
        switch (key) {
            case JTI_KEY:
            case USER_ID_KEY:
                validate(value, UUID.class);
                break;
            case EXP_KEY:
                validate(value, Instant.class);
                break;
            case CLIENT_ID_KEY:
            case USERNAME_KEY:
                validate(value, String.class);
                break;
            case TOKEN_TYPE_KEY:
                validate(value, JwtTokenType.class);
                break;
            case SCOPE_KEY:
            case AUTHORITIES_KEY:
                validate(value, Set.class);
                break;
            default:
                break;

        }
    }
}
