/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-19
 */
package io.agatsenko.todo.service.auth.security.oauth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import lombok.NonNull;

import io.agatsenko.todo.service.auth.model.OAuth2Token;
import io.agatsenko.todo.service.auth.model.User;
import io.agatsenko.todo.service.auth.security.oauth.jwt.*;
import io.agatsenko.todo.util.Check;

public class MongoTokenServices
        implements AuthorizationServerTokenServices, ResourceServerTokenServices, ConsumerTokenServices {
    private final int accessTokenValiditySeconds;
    private final int refreshTokenValiditySeconds;
    private final AuthenticationKeyGenerator authKeyGenerator;
    private final JwtTokenValueFactory tokenValueFactory;
    private final MongoTokenStore tokenStore;
    private final AuthenticationManager authManager;
    private final ClientDetailsService clientDetailsService;

    public MongoTokenServices(
            int accessTokenValiditySeconds,
            int refreshTokenValiditySeconds,
            @NonNull AuthenticationKeyGenerator authKeyGenerator,
            @NonNull JwtTokenValueFactory tokenValueFactory,
            @NonNull MongoTokenStore tokenStore,
            AuthenticationManager authManager,
            ClientDetailsService clientDetailsService) {
        Check.arg(accessTokenValiditySeconds > 0, "accessTokenValiditySeconds should greater than 0");
        Check.arg(refreshTokenValiditySeconds > 0, "refreshTokenValiditySeconds should greater than 0");
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
        this.authKeyGenerator = authKeyGenerator;
        this.tokenValueFactory = tokenValueFactory;
        this.tokenStore = tokenStore;
        this.authManager = authManager;
        this.clientDetailsService = clientDetailsService;
    }

    @Override
    public OAuth2AccessToken createAccessToken(OAuth2Authentication auth) throws AuthenticationException {
        OAuth2AccessToken accessToken = null;
        final var existingAccessToken = tokenStore.getAccessToken(auth);
        if (existingAccessToken != null) {
            if (existingAccessToken.isExpired()) {
                tokenStore.removeAccessToken(existingAccessToken);
                if (existingAccessToken.getRefreshToken() != null) {
                    tokenStore.removeRefreshToken(accessToken.getRefreshToken());
                }
            }
            else {
                accessToken = existingAccessToken;
            }
        }
        if (accessToken == null) {
            accessToken = generateAndStoreAccessToken(auth);
        }
        return accessToken;
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication auth) {
        return tokenStore.getAccessToken(auth);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessTokenValue) {
        return tokenStore.readAccessToken(accessTokenValue);
    }

    @Override
    public OAuth2AccessToken refreshAccessToken(String refreshTokenValue, TokenRequest tokenRequest)
            throws AuthenticationException {
        OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(refreshTokenValue);
        if (refreshToken == null) {
            throw new InvalidGrantException("Invalid refresh token: " + refreshTokenValue);
        }
        OAuth2Authentication auth = tokenStore.readAuthenticationForRefreshToken(refreshToken);
        if (this.authManager != null && !auth.isClientOnly()) {
            Authentication user = new PreAuthenticatedAuthenticationToken(
                    auth.getUserAuthentication(),
                    "",
                    auth.getAuthorities()
            );
            user = authManager.authenticate(user);
            Object details = auth.getDetails();
            auth = new OAuth2Authentication(auth.getOAuth2Request(), user);
            auth.setDetails(details);
        }
        String clientId = auth.getOAuth2Request().getClientId();
        if (clientId == null || !clientId.equals(tokenRequest.getClientId())) {
            throw new InvalidGrantException("Wrong client for this refresh token: " + refreshTokenValue);
        }
        auth = createRefreshedAuthentication(auth, tokenRequest);
        tokenStore.removeAccessTokenUsingRefreshToken(refreshToken);
        tokenStore.removeRefreshToken(refreshToken);
        return generateAndStoreAccessToken(auth);
    }

    @Override
    public boolean revokeToken(String accessTokenValue) {
        final var accessToken = tokenStore.readAccessToken(accessTokenValue);
        if (accessToken == null) {
            return false;
        }
        if (accessToken.getRefreshToken() != null) {
            tokenStore.removeRefreshToken(accessToken.getRefreshToken());
        }
        tokenStore.removeAccessToken(accessToken);
        return true;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessTokenValue)
            throws AuthenticationException, InvalidTokenException {
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenValue);
        if (accessToken == null) {
            throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
        }
        else if (accessToken.isExpired()) {
            tokenStore.removeAccessToken(accessToken);
            throw new InvalidTokenException("Access token expired: " + accessTokenValue);
        }

        OAuth2Authentication auth = tokenStore.readAuthentication(accessToken);
        if (auth == null) {
            throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
        }
        if (clientDetailsService != null) {
            String clientId = auth.getOAuth2Request().getClientId();
            try {
                clientDetailsService.loadClientByClientId(clientId);
            }
            catch (ClientRegistrationException e) {
                throw new InvalidTokenException("Client not valid: " + clientId, e);
            }
        }
        return auth;
    }

    private String extractClientId(OAuth2Authentication auth) {
        return auth.getOAuth2Request().getClientId();
    }

    private UUID extractUserId(OAuth2Authentication auth) {
        if (auth.getUserAuthentication() == null || !(auth.getUserAuthentication().getPrincipal() instanceof User)) {
            return null;
        }
        return ((User) auth.getUserAuthentication().getPrincipal()).getId();
    }

    private String extractUsername(OAuth2Authentication auth) {
        if (auth.getUserAuthentication() == null || auth.getUserAuthentication().getPrincipal() == null) {
            return null;
        }
        return auth.getUserAuthentication().getPrincipal() instanceof User ?
                ((User) auth.getUserAuthentication().getPrincipal()).getUsername() :
                auth.getUserAuthentication().getPrincipal().toString();
    }

    private Set<String> extractScope(OAuth2Authentication auth) {
        return auth.getOAuth2Request().getScope();
    }

    private Set<String> extractAuthorities(OAuth2Authentication auth) {
        final var authorities = new HashSet<String>();
        authorities.addAll(
                auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())
        );
        if (auth.getUserAuthentication() != null) {
            authorities.addAll(
                    auth.getUserAuthentication().getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList())
            );
        }
        return authorities;
    }

    private JwtToken buildJwtToken(UUID tokenId, JwtTokenType tokenType, Instant exp, OAuth2Authentication auth) {
        return JwtToken.builder()
                .header(JwtHeader.builder().typ(JwtHeader.TYP_JWT_VALUE).build())
                .payload(
                        JwtPayload.builder()
                                .jti(tokenId)
                                .exp(exp)
                                .tokenType(tokenType)
                                .clientId(extractClientId(auth))
                                .userId(extractUserId(auth))
                                .username(extractUsername(auth))
                                .scope(extractScope(auth))
                                .authorities(extractAuthorities(auth))
                                .build()

                )
                .build(tokenValueFactory);
    }

    private OAuth2AccessToken generateAndStoreAccessToken(OAuth2Authentication auth) {
        final var now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        final var accessTokenId = UUID.randomUUID();
        final var accessTokenExpTime = now.plusSeconds(accessTokenValiditySeconds);
        final var refreshTokenId = UUID.randomUUID();
        final var refreshTokenExpTime = now.plusSeconds(refreshTokenValiditySeconds);
        final var token = OAuth2Token.builder()
                .authenticationKey(authKeyGenerator.extractKey(auth))
                .authentication(auth)
                .clientId(auth.getOAuth2Request().getClientId())
                .userId(extractUserId(auth))
                .username(extractUsername(auth))
                .accessTokenId(accessTokenId)
                .accessTokenValue(
                        buildJwtToken(accessTokenId, JwtTokenType.ACCESS, accessTokenExpTime, auth).getTokenValue()
                )
                .accessTokenExpirationTime(accessTokenExpTime)
                .refreshTokenId(refreshTokenId)
                .refreshTokenValue(
                        buildJwtToken(refreshTokenId, JwtTokenType.REFRESH, refreshTokenExpTime, auth).getTokenValue()
                )
                .refreshTokenExpirationTime(refreshTokenExpTime)
                .scope(extractScope(auth))
                .build();
        final var accessToken = token.toAccessToken().get();
        tokenStore.storeAccessToken(accessToken, auth);
        return accessToken;
    }

    private OAuth2Authentication createRefreshedAuthentication(OAuth2Authentication auth, TokenRequest request) {
        OAuth2Authentication narrowed;
        Set<String> scope = request.getScope();
        OAuth2Request clientAuth = auth.getOAuth2Request().refresh(request);
        if (scope != null && !scope.isEmpty()) {
            Set<String> originalScope = clientAuth.getScope();
            if (originalScope == null || !originalScope.containsAll(scope)) {
                throw new InvalidScopeException(
                        "Unable to narrow the scope of the client authentication to " + scope + ".",
                        originalScope
                );
            }
            else {
                clientAuth = clientAuth.narrowScope(scope);
            }
        }
        narrowed = new OAuth2Authentication(clientAuth, auth.getUserAuthentication());
        return narrowed;
    }
}
