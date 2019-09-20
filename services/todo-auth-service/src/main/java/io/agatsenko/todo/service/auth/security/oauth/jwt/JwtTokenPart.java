/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-19
 */
package io.agatsenko.todo.service.auth.security.oauth.jwt;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
abstract class JwtTokenPart {
    private final Map<String, Object> values;

    public JwtTokenPart(Map<String, Object> values) {
        this.values = values == null ? Collections.emptyMap() : Map.copyOf(values);
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public <T> Optional<T> get(String key, Class<T> valueClass) {
        return Optional.ofNullable(valueClass.cast(values.get(key)));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + '(' + values.toString() + ')';
    }
}
