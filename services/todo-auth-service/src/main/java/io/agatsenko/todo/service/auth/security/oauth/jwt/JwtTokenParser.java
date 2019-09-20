/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-20
 */
package io.agatsenko.todo.service.auth.security.oauth.jwt;

public interface JwtTokenParser {
    JwtToken parse(String jwtToken);
}
