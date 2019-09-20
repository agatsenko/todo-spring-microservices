/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-19
 */
package io.agatsenko.todo.service.auth.model;

import java.util.Date;

import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import lombok.NonNull;
import lombok.Value;

import io.agatsenko.todo.util.Check;

@Value
public class OAuth2RefreshTokenAdapter implements ExpiringOAuth2RefreshToken {
    private final OAuth2Token token;

    OAuth2RefreshTokenAdapter(@NonNull OAuth2Token token) {
        Check.arg(token.isRefreshTokenDefined(), "refresh token is not defined");
        this.token = token;
    }

    @Override
    public String getValue() {
        return token.getRefreshTokenValue();
    }

    public boolean isExpired() {
        return token.isRefreshTokenExpired();
    }

    @Override
    public Date getExpiration() {
        return new Date(token.getAccessTokenExpirationTime().toEpochMilli());
    }
}
