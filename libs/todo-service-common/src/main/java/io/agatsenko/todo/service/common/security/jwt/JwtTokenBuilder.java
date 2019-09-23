/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-19
 */
package io.agatsenko.todo.service.common.security.jwt;

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
        final var tokenValue = tokenValueFactory.create(header, payload);
        return new JwtTokenImpl(
                header,
                payload,
                tokenValue,
                JwtTokenSplitter.defaultSplitter.split(tokenValue)._3
        );
    }
}
