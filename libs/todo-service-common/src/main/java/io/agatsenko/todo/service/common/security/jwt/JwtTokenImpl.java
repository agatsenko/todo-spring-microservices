/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-20
 */
package io.agatsenko.todo.service.common.security.jwt;

import java.util.Optional;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(of = "tokenValue")
@ToString(of = "tokenValue")
public class JwtTokenImpl implements JwtToken {
    private final JwtHeader header;

    private final JwtPayload payload;

    private final String tokenValue;

    private final String signature;

    public JwtTokenImpl(
            @NonNull JwtHeader header,
            @NonNull JwtPayload payload,
            @NonNull String tokenValue,
            String signature) {
        this.header = header;
        this.payload = payload;
        this.signature = signature;
        this.tokenValue = tokenValue;
    }

    @Override
    public JwtHeader getHeader() {
        return header;
    }

    @Override
    public JwtPayload getPayload() {
        return payload;
    }

    @Override
    public String getTokenValue() {
        return tokenValue;
    }

    @Override
    public Optional<String> getSignature() {
        return Optional.ofNullable(signature);
    }
}
