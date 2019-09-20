/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-20
 */
package io.agatsenko.todo.service.auth.security.oauth.jwt;

import java.util.Optional;
import java.util.regex.Pattern;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(of = "tokenValue")
@ToString(of = "tokenValue")
public class JwtTokenImpl implements JwtToken {
    private static final String SIGNATURE_REGEX_GROUP_NAME = "signature";

    private static final Pattern signatureRegexPattern = Pattern.compile("[^.]*\\.[^.]*(\\.(?<signature>[^.]+)?)?");

    private final JwtHeader header;

    private final JwtPayload payload;

    private final String signature;

    private final String tokenValue;

    public JwtTokenImpl(
            @NonNull JwtHeader header,
            @NonNull JwtPayload payload,
            @NonNull String tokenValue) {
        this.header = header;
        this.payload = payload;
        this.tokenValue = tokenValue;
        this.signature = extractSignature(tokenValue);
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
    public Optional<String> getSignature() {
        return Optional.ofNullable(signature);
    }

    @Override
    public String getTokenValue() {
        return tokenValue;
    }

    private static String extractSignature(String tokenValue) {
        final var matcher = signatureRegexPattern.matcher(tokenValue);
        return matcher.matches() ? matcher.group(SIGNATURE_REGEX_GROUP_NAME) : null;
    }
}
