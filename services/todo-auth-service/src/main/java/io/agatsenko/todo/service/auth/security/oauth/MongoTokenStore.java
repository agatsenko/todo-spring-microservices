/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-19
 */
package io.agatsenko.todo.service.auth.security.oauth;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import io.agatsenko.todo.service.auth.model.OAuth2AccessTokenAdapter;
import io.agatsenko.todo.service.auth.model.OAuth2Token;
import io.agatsenko.todo.service.auth.model.OAuth2TokenRepo;
import io.agatsenko.todo.service.auth.security.oauth.jwt.JwtPayload;
import io.agatsenko.todo.service.auth.security.oauth.jwt.JwtTokenParser;
import io.agatsenko.todo.service.auth.security.oauth.jwt.JwtTokenType;

@RequiredArgsConstructor
public class MongoTokenStore implements TokenStore {
    @NonNull
    private final JwtTokenParser tokenParser;
    @NonNull
    private final AuthenticationKeyGenerator authKeyGenerator;
    @NonNull
    private final OAuth2TokenRepo tokenRepo;

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken accessToken) {
        return readAuthentication(accessToken.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String accessTokenValue) {
        return readTokenByAccessTokenValue(accessTokenValue)
                .map(OAuth2Token::getAuthentication)
                .orElse(null);
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken accessToken, OAuth2Authentication auth) {
        if (accessToken instanceof OAuth2AccessTokenAdapter) {
            final var token = ((OAuth2AccessTokenAdapter) accessToken).getToken();
            if (!Objects.equals(token.getAuthentication(), auth)) {
                throw new InvalidTokenException(
                        "the token authentication and the passed authentication does not match"
                );
            }
            tokenRepo.save(token);
        }
        else {
            throw new InvalidTokenException(
                    String.format("%s token type is unsupported", accessToken.getClass().getSimpleName())
            );
        }
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessTokenValue) {
        return readTokenByAccessTokenValue(accessTokenValue).flatMap(OAuth2Token::toAccessToken).orElse(null);
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken accessToken) {
        readTokenByAccessTokenValue(accessToken.getValue()).ifPresent(token -> {
            var modifiedToken = token.clearAccessToken();
            if (!removeIfEmpty(modifiedToken)) {
                tokenRepo.save(token);
            }
        });
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication auth) {
        throw new UnsupportedOperationException();
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String refreshTokenValue) {
        return readTokenByRefreshTokenValue(refreshTokenValue).flatMap(OAuth2Token::toRefreshToken).orElse(null);
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken refreshToken) {
        return readTokenByRefreshTokenValue(refreshToken.getValue()).map(OAuth2Token::getAuthentication).orElse(null);
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
        readTokenByRefreshTokenValue(refreshToken.getValue()).ifPresent(token -> {
            var modifiedToken = token.clearRefreshToken();
            if (!removeIfEmpty(modifiedToken)) {
                tokenRepo.save(modifiedToken);
            }
        });
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        readTokenByRefreshTokenValue(refreshToken.getValue()).ifPresent(token -> {
            var modifiedToken = token.clearAccessToken();
            if (!removeIfEmpty(modifiedToken)) {
                tokenRepo.save(modifiedToken);
            }
        });
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication auth) {
        return tokenRepo.findById(authKeyGenerator.extractKey(auth)).flatMap(OAuth2Token::toAccessToken).orElse(null);
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String username) {
        return tokenRepo.findByClientIdAndUsername(clientId, username).stream()
                .map(token -> token.toAccessToken().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        return tokenRepo.findByClientId(clientId).stream()
                .map(token -> token.toAccessToken().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private boolean removeIfEmpty(OAuth2Token token) {
        if (token.isEmpty()) {
            tokenRepo.delete(token);
            return true;
        }
        return false;
    }

    private JwtPayload parsePayload(String tokenValue) {
        return tokenParser.parse(tokenValue).getPayload();
    }

    private JwtPayload extractAccessTokenPayload(String accessTokenValue) {
        final var payload = parsePayload(accessTokenValue);
        if (payload.getTokenType().orElse(null) != JwtTokenType.ACCESS) {
            throw new InvalidTokenException("Encoded token is not a access token");
        }
        return payload;
    }

    private Optional<OAuth2Token> readTokenByAccessTokenValue(String accessTokenValue) {
        final var payload = extractAccessTokenPayload(accessTokenValue);
        return payload.getJti().flatMap(jti ->
                tokenRepo.findByAccessTokenId(jti).map(token -> removeIfEmpty(token) ? null : token)
        );
    }

    private JwtPayload extractRefreshTokenPayload(String refreshTokenValue) {
        final var payload = parsePayload(refreshTokenValue);
        if (payload.getTokenType().orElse(null) != JwtTokenType.REFRESH) {
            throw new InvalidTokenException("Encoded token is not a refresh token");
        }
        return payload;
    }

    private Optional<OAuth2Token> readTokenByRefreshTokenValue(String refreshTokenValue) {
        final var payload = extractRefreshTokenPayload(refreshTokenValue);
        return payload.getJti().flatMap(jti ->
                tokenRepo.findByRefreshTokenId(jti).map(token -> removeIfEmpty(token) ? null : token)
        );
    }
}
