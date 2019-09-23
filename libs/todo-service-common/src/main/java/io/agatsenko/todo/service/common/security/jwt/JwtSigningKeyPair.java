/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-20
 */
package io.agatsenko.todo.service.common.security.jwt;

import java.security.Key;
import java.security.KeyPair;
import java.util.Optional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtSigningKeyPair implements JwtSigningKeys {
    @NonNull
    private final KeyPair keyPair;

    @Override
    public Optional<Key> getSigningKey() {
        return Optional.of(keyPair.getPrivate());
    }

    @Override
    public Optional<Key> getVerifyingKey() {
        return Optional.of(keyPair.getPublic());
    }
}
