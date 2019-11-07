/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-19
 */
package io.agatsenko.todo.service.auth.model;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import io.agatsenko.todo.util.Check;

@Document("oauth2_tokens")
@Value
public class OAuth2Token {
    @Id
    private final String authenticationKey;
    private final OAuth2Authentication authentication;

    private final String clientId;
    private final UUID userId;
    private final String username;

    private final UUID accessTokenId;
    private final String accessTokenValue;
    private final Instant accessTokenExpirationTime;

    private final UUID refreshTokenId;
    private final String refreshTokenValue;
    private final Instant refreshTokenExpirationTime;

    private final Set<String> scope;

    @Builder(toBuilder = true)
    private OAuth2Token(
            @NotNull String authenticationKey,
            @NonNull OAuth2Authentication authentication,
            @NonNull String clientId,
            UUID userId,
            String username,
            UUID accessTokenId,
            String accessTokenValue,
            Instant accessTokenExpirationTime,
            UUID refreshTokenId,
            String refreshTokenValue,
            Instant refreshTokenExpirationTime,
            @Singular("scope") Set<String> scope) {
        if (accessTokenId != null) {
            Check.argNotEmpty(accessTokenValue, "accessTokenValue");
            Check.argNotNull(accessTokenExpirationTime, "accessTokenExpirationTime");
        }
        if (refreshTokenId != null) {
            Check.argNotEmpty(refreshTokenValue, "refreshTokenValue");
            Check.argNotNull(refreshTokenExpirationTime, "refreshTokenExpirationTime");
        }
        this.authenticationKey = authenticationKey;
        this.authentication = authentication;
        this.clientId = clientId;
        this.userId = userId;
        this.username = username;
        this.accessTokenId = accessTokenId;
        this.accessTokenValue = accessTokenValue;
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenId = refreshTokenId;
        this.refreshTokenValue = refreshTokenValue;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
        this.scope = scope == null ? Collections.emptySet() : Set.copyOf(scope);
    }

    public boolean isAccessTokenDefined() {
        return accessTokenValue != null;
    }

    public boolean isAccessTokenExpired() {
        return !isAccessTokenDefined() || (accessTokenExpirationTime.toEpochMilli() - System.currentTimeMillis()) <= 0;
    }

    public OAuth2Token clearAccessToken() {
        return toBuilder()
                .accessTokenId(null)
                .accessTokenValue(null)
                .accessTokenExpirationTime(null)
                .build();
    }

    public Optional<OAuth2AccessToken> toAccessToken() {
        return isAccessTokenDefined() ? Optional.of(new OAuth2AccessTokenAdapter(this)) : Optional.empty();
    }

    public boolean isRefreshTokenDefined() {
        return refreshTokenValue != null;
    }

    public boolean isRefreshTokenExpired() {
        return !isRefreshTokenDefined()
               || (refreshTokenExpirationTime.toEpochMilli() - System.currentTimeMillis()) <= 0;
    }

    public OAuth2Token clearRefreshToken() {
        return toBuilder()
                .refreshTokenId(null)
                .refreshTokenValue(null)
                .refreshTokenExpirationTime(null)
                .build();
    }

    public Optional<ExpiringOAuth2RefreshToken> toRefreshToken() {
        return isRefreshTokenDefined() ? Optional.of(new OAuth2RefreshTokenAdapter(this)) : Optional.empty();
    }

    public boolean isEmpty() {
        return !isAccessTokenDefined() && !isRefreshTokenDefined();
    }
}
