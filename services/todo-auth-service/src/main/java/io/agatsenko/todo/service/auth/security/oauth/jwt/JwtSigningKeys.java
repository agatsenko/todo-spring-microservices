/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-20
 */
package io.agatsenko.todo.service.auth.security.oauth.jwt;

import java.security.Key;

public interface JwtSigningKeys {
    Key getSigningKey();

    Key getVerifyingKey();
}
