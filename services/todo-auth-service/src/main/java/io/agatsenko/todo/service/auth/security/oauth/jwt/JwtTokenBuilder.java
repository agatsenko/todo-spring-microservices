/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-19
 */
package io.agatsenko.todo.service.auth.security.oauth.jwt;

import java.util.Optional;

import lombok.NonNull;

import io.agatsenko.todo.util.Check;

public class JwtTokenBuilder {
    private JwtHeader header;
    private JwtPayload payload;

    public JwtTokenBuilder header(JwtHeader header) {
        this.header = header;
        return this;
    }

    public Optional<JwtHeader> getHeader() {
        return Optional.ofNullable(header);
    }

    public JwtTokenBuilder payload(JwtPayload payload) {
        this.payload = payload;
        return this;
    }

    public Optional<JwtPayload> getPayload() {
        return Optional.ofNullable(payload);
    }

    public JwtToken build(@NonNull JwtTokenValueFactory tokenValueFactory) {
        Check.state(header != null, "header is not defined");
        Check.state(payload != null, "payload is not defined");
        return new JwtTokenImpl(header, payload, tokenValueFactory.create(header, payload));
    }
}
