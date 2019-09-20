/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-19
 */
package io.agatsenko.todo.service.auth.model;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import lombok.NonNull;
import lombok.Value;

import io.agatsenko.todo.util.Check;

@Value
public class OAuth2AccessTokenAdapter implements OAuth2AccessToken {
    private final OAuth2Token token;
    private final OAuth2RefreshTokenAdapter refreshToken;

    OAuth2AccessTokenAdapter(@NonNull OAuth2Token token) {
        Check.arg(token.isAccessTokenDefined(), "access token is not defined");
        this.token = token;
        this.refreshToken = token.isRefreshTokenDefined() ? new OAuth2RefreshTokenAdapter(token) : null;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return Collections.emptyMap();
    }

    @Override
    public Set<String> getScope() {
        return token.getScope();
    }

    @Override
    public OAuth2RefreshToken getRefreshToken() {
        return refreshToken.isExpired() ? null : refreshToken;
    }

    @Override
    public String getTokenType() {
        return BEARER_TYPE;
    }

    @Override
    public boolean isExpired() {
        return token.isAccessTokenExpired();
    }

    @Override
    public Date getExpiration() {
        return new Date(token.getAccessTokenExpirationTime().toEpochMilli());
    }

    @Override
    public int getExpiresIn() {
        return (int) ((token.getAccessTokenExpirationTime().toEpochMilli() - System.currentTimeMillis()) / 1000L);
    }

    @Override
    public String getValue() {
        return token.getAccessTokenValue();
    }
}
