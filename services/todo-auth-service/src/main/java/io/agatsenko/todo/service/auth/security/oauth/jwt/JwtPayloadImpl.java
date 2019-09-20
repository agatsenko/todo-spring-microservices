/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-19
 */
package io.agatsenko.todo.service.auth.security.oauth.jwt;

import java.time.Instant;
import java.util.*;

public class JwtPayloadImpl extends JwtTokenPart implements JwtPayload {
    public JwtPayloadImpl(Map<String, Object> values) {
        super(values);
    }

    @Override
    public Optional<UUID> getJti() {
        return get(JTI_KEY, UUID.class);
    }

    @Override
    public Optional<Instant> getExp() {
        return get(EXP_KEY, Instant.class);
    }

    @Override
    public Optional<JwtTokenType> getTokenType() {
        return get(TOKEN_TYPE_KEY, JwtTokenType.class);
    }

    @Override
    public Optional<String> getClientId() {
        return get(CLIENT_ID_KEY, String.class);
    }

    @Override
    public Optional<UUID> getUserId() {
        return get(USER_ID_KEY, UUID.class);
    }

    @Override
    public Optional<String> getUsername() {
        return get(USERNAME_KEY, String.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<String> getScope() {
        return get(SCOPE_KEY, Set.class).map(set -> (Set<String>)set).orElse(Collections.emptySet());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<String> getAuthorities() {
        return get(AUTHORITIES_KEY, Set.class).map(set -> (Set<String>)set).orElse(Collections.emptySet());
    }

    @Override
    public Map<String, Object> toMap() {
        return getValues();
    }
}
