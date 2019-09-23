/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-20
 */
package io.agatsenko.todo.service.common.security.jwt;

public interface JwtTokenParser {
    JwtToken parse(String jwtTokenValue, boolean checkSignature);

    default JwtToken parse(String jwtTokenValue) {
        return parse(jwtTokenValue, true);
    }
}
