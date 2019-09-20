/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-20
 */
package io.agatsenko.todo.service.auth.security.oauth.jwt.jjwt;

import java.time.Instant;
import java.util.*;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import io.agatsenko.todo.service.auth.security.oauth.jwt.*;
import io.agatsenko.todo.util.Check;

import static io.agatsenko.todo.service.auth.security.oauth.jwt.JwtPayload.*;

@RequiredArgsConstructor
public class JjwtTokenParser implements JwtTokenParser {
    @NonNull
    private final JwtSigningKeys signingKeys;

    @SuppressWarnings("unchecked")
    @Override
    public JwtToken parse(String jwtTokenValue) {
        Check.argNotEmpty(jwtTokenValue, "jwtTokenValue");
        try {
            final var jjwt = Jwts.parser().setSigningKey(signingKeys.getVerifyingKey()).parse(jwtTokenValue);
            return new JwtTokenImpl(
                    createHeader(jjwt.getHeader()),
                    createPayload((Map<String, Object>) jjwt.getBody()),
                    jwtTokenValue
            );
        }
        catch (ExpiredJwtException ex) {
            return new JwtTokenImpl(
                    createHeader(ex.getHeader()),
                    createPayload(ex.getClaims()),
                    jwtTokenValue
            );
        }
    }

    private static JwtHeader createHeader(Map<String, Object> jjwtHeader) {
        return new JwtHeaderImpl(jjwtHeader);
    }

    private static UUID extractJti(Map<String, Object> jjwtPayload) {
        final var strValue = (String) jjwtPayload.get(JTI_KEY);
        return strValue == null ? null : UUID.fromString(strValue);
    }

    private static Instant extractExp(Map<String, Object> jjwtPayload) {
        final var intValue = (Integer) jjwtPayload.get(EXP_KEY);
        return intValue == null ? null : Instant.ofEpochSecond(intValue.longValue());
    }

    private static JwtTokenType extractTokenType(Map<String, Object> jjwtPayload) {
        final var strValue = (String) jjwtPayload.get(TOKEN_TYPE_KEY);
        return JwtTokenType.fromValue(strValue).orElse(null);
    }

    private static UUID extractUserId(Map<String, Object> jjwtPayload) {
        final var strValue = (String) jjwtPayload.get(USER_ID_KEY);
        return strValue == null ? null : UUID.fromString(strValue);
    }

    @SuppressWarnings("unchecked")
    private static Set<String> extractScope(Map<String, Object> jjwtPayload) {
        final var collection = (Collection<String>) jjwtPayload.get(SCOPE_KEY);
        return collection == null ? Collections.emptySet() : Set.copyOf(collection);
    }

    @SuppressWarnings("unchecked")
    private static Set<String> extractAuthorities(Map<String, Object> jjwtPayload) {
        final var collection = (Collection<String>) jjwtPayload.get(AUTHORITIES_KEY);
        return collection == null ? Collections.emptySet() : Set.copyOf(collection);
    }

    private static void replaceValue(Map<String, Object> map, String key, Object value) {
        if (value == null) {
            map.remove(key);
        }
        else {
            map.put(key, value);
        }
    }

    private static JwtPayload createPayload(Map<String, Object> jjwtPayload) {
        Map<String, Object> payloadMap = new HashMap<>(jjwtPayload);
        replaceValue(payloadMap, JTI_KEY, extractJti(jjwtPayload));
        replaceValue(payloadMap, EXP_KEY, extractExp(jjwtPayload));
        replaceValue(payloadMap, TOKEN_TYPE_KEY, extractTokenType(jjwtPayload));
        replaceValue(payloadMap, USER_ID_KEY, extractUserId(jjwtPayload));
        replaceValue(payloadMap, SCOPE_KEY, extractScope(jjwtPayload));
        replaceValue(payloadMap, AUTHORITIES_KEY, extractAuthorities(jjwtPayload));
        return new JwtPayloadImpl(payloadMap);
    }
}
