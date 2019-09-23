/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-22
 */
package io.agatsenko.todo.service.common.security.jwt;

import java.security.Key;
import java.util.Optional;

final class EmptyJwtSigningKeys implements JwtSigningKeys {
    public static final EmptyJwtSigningKeys instance = new EmptyJwtSigningKeys();

    private EmptyJwtSigningKeys() {
        // do nothing
    }

    @Override
    public Optional<Key> getSigningKey() {
        return Optional.empty();
    }

    @Override
    public Optional<Key> getVerifyingKey() {
        return Optional.empty();
    }
}
