/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-19
 */
package io.agatsenko.todo.service.common.security.jwt;

import java.util.Objects;
import java.util.Optional;

public enum JwtTokenType {
    ACCESS("access"),
    REFRESH("refresh");

    private final String value;

    JwtTokenType(String value) {
        this.value = value;
    }

    public static Optional<JwtTokenType> fromValue(String value) {
        JwtTokenType foundType = null;
        for (final var  type : values()) {
            if (Objects.equals(type.getValue(), value)) {
                foundType = type;
                break;
            }
        }
        return Optional.ofNullable(foundType);
    }

    public String getValue() {
        return value;
    }
}
