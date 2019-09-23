/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-22
 */
package io.agatsenko.todo.service.common.security.jwt;

import java.util.regex.Pattern;

import io.agatsenko.todo.util.Check;
import io.agatsenko.todo.util.functional.Tuple3;

public class DefaultJwtTokenSplitter implements JwtTokenSplitter {
    private static final String HEADER_GROUP = "header";
    private static final String PAYLOAD_GROUP = "payload";
    private static final String SIGNATURE_GROUP = "signature";

    private static final Pattern decodePattern = Pattern.compile(
            "(?<header>[^.]+)\\.(?<payload>[^.]+)?\\.(?<signature>[^.]+)?"
    );

    @Override
    public Tuple3<String, String, String> split(String jwtTokenValue) {
        Check.argNotEmpty(jwtTokenValue, "jwtTokenValue");
        final var matcher = decodePattern.matcher(jwtTokenValue);
        Check.state(matcher.matches(), "token value is not match to jwt token");
        return Tuple3.of(matcher.group(HEADER_GROUP), matcher.group(PAYLOAD_GROUP), matcher.group(SIGNATURE_GROUP));
    }
}
