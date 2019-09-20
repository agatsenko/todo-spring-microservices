/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-20
 */
package io.agatsenko.todo.service.auth.security.oauth.jwt;

import java.security.Key;
import java.security.KeyPair;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtSigningKeyPair implements JwtSigningKeys {
    @NonNull
    private final KeyPair keyPair;

    @Override
    public Key getSigningKey() {
        return keyPair.getPrivate();
    }

    @Override
    public Key getVerifyingKey() {
        return keyPair.getPublic();
    }
}
