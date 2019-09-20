/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-19
 */
package io.agatsenko.todo.service.auth.security.oauth.jwt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import io.agatsenko.todo.util.Check;

import static io.agatsenko.todo.service.auth.security.oauth.jwt.JwtHeader.ALG_KEY;
import static io.agatsenko.todo.service.auth.security.oauth.jwt.JwtHeader.TYP_KEY;

public class JwtHeaderBuilder {
    private final Map<String, Object> values = new HashMap<>();

    public JwtHeaderBuilder typ(String value) {
        return put(TYP_KEY, value);
    }

    public Optional<String> getTyp() {
        return get(TYP_KEY, String.class);
    }

    public JwtHeaderBuilder alg(String value) {
        return put(ALG_KEY, value);
    }

    public Optional<String> getAlg() {
        return get(ALG_KEY, String.class);
    }

    public JwtHeaderBuilder put(String key, Object value) {
        validate(key, value);
        if (value == null || (value instanceof String && ((String)value).isEmpty())) {
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

    public JwtHeaderBuilder clear() {
        values.clear();
        return this;
    }

    public JwtHeader build() {
        return new JwtHeaderImpl(values);
    }

    private void validate(String key, Object value) {
        Check.argNotEmpty(key, "key");
        if (value == null) {
            return;
        }
        if (Objects.equals(key, ALG_KEY) || Objects.equals(key, TYP_KEY)) {
            Check.arg(value instanceof String, "expected that %s value to be %s", value, String.class.getName());
        }
    }
}
