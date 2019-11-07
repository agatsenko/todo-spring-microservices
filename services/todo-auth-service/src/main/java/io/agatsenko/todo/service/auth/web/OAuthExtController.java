/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-11-04
 */
package io.agatsenko.todo.service.auth.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.base.Strings;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth/ext/token")
public class OAuthExtController {
    @NonNull
    private final TokenStore tokenStore;

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeToken(HttpServletRequest request) {
        final var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Strings.isNullOrEmpty(authHeader) || !authHeader.startsWith(OAuth2AccessToken.BEARER_TYPE)) {
            throw new UnauthorizedUserException("Authorization header is not defined");
        }
        final var tokenStr = authHeader.replace(OAuth2AccessToken.BEARER_TYPE, "").trim();
        final var accessToken = tokenStore.readAccessToken(tokenStr);
        if (accessToken.getRefreshToken() != null) {
            tokenStore.removeRefreshToken(accessToken.getRefreshToken());
        }
        tokenStore.removeAccessToken(accessToken);
    }
}
