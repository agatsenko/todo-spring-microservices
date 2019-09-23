/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-20
 */
package io.agatsenko.todo.service.common.security.jwt;

import java.security.Key;
import java.util.Optional;

public interface JwtSigningKeys {
    JwtSigningKeys empty = EmptyJwtSigningKeys.instance;

    Optional<Key> getSigningKey();

    Optional<Key> getVerifyingKey();
}
