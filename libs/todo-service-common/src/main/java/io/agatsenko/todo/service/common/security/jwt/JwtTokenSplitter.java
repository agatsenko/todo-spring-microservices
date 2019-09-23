/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-22
 */
package io.agatsenko.todo.service.common.security.jwt;

import io.agatsenko.todo.util.functional.Tuple3;

public interface JwtTokenSplitter {
    String PART_DELIMITER = ".";

    JwtTokenSplitter defaultSplitter = new DefaultJwtTokenSplitter();

    Tuple3<String, String, String> split(String jwtTokenValue);
}
