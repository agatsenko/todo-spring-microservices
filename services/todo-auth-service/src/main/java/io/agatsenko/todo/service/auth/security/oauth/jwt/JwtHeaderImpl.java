/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-19
 */
package io.agatsenko.todo.service.auth.security.oauth.jwt;

import java.util.Map;
import java.util.Optional;

public class JwtHeaderImpl extends JwtTokenPart implements JwtHeader {
    public JwtHeaderImpl(Map<String, Object> values) {
        super(values);
    }

    @Override
    public Optional<String> getTyp() {
        return get(TYP_KEY, String.class);
    }

    @Override
    public Optional<String> getAlg() {
        return get(ALG_KEY, String.class);
    }

    @Override
    public Map<String, Object> toMap() {
        return getValues();
    }
}
