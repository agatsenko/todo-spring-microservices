/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-20
 */
package io.agatsenko.todo.service.common.security.jwt.jjwt;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import io.agatsenko.todo.service.common.security.jwt.*;

@RequiredArgsConstructor
public class JjwtTokenValueFactory implements JwtTokenValueFactory {
    @NonNull
    private final JwtSigningKeys signingKeys;

    @Override
    public String create(JwtHeader header, JwtPayload payload) {
        final var builder = Jwts.builder()
                .setHeader(toJjwtMap(header.toMap()))
                .setClaims(toJjwtMap(payload.toMap()));
        signingKeys.getSigningKey().ifPresent(builder::signWith);
        return builder.compact();
    }

    private static Object toJjwtValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Instant) {
            return new Date(((Instant) value).toEpochMilli());
        }
        if (value instanceof JwtTokenType) {
            return ((JwtTokenType) value).getValue();
        }
        return value;
    }

    private static Map<String, Object> toJjwtMap(Map<String, Object> map) {
        final var jjwtMap = new HashMap<String, Object>(map.size());
        for (final var entry : map.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            jjwtMap.put(entry.getKey(), toJjwtValue(entry.getValue()));
        }
        return jjwtMap;
    }
}
