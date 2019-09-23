/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-22
 */
package io.agatsenko.todo.service.list.service;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import com.google.common.base.Strings;
import feign.FeignException;
import lombok.AllArgsConstructor;

import io.agatsenko.todo.service.auth.client.openfiegn.UserClient;
import io.agatsenko.todo.service.common.security.jwt.JwtTokenParser;
import io.agatsenko.todo.util.Throwables;
import io.agatsenko.todo.util.functional.CheckedFunction0;

@AllArgsConstructor
public class DefaultUserIdResolver implements UserIdResolver {
    private final JwtTokenParser jwtTokenParser;
    private final UserClient userClient;

    @Override
    public Optional<UUID> resolve(Principal principal) {
        Optional<UUID> userId = Optional.empty();
        if (principal instanceof OAuth2Authentication) {
            userId = resolve((OAuth2Authentication) principal);
        }
        if (userId.isEmpty()) {
            userId = resolve(principal.getName());
        }
        return userId;
    }

    private Optional<UUID> resolve(OAuth2Authentication auth) {
        if (auth.getDetails() == null || !(auth.getDetails() instanceof OAuth2AuthenticationDetails)) {
            return null;
        }
        final var tokenValue = ((OAuth2AuthenticationDetails) auth.getDetails()).getTokenValue();
        if (!Strings.isNullOrEmpty(tokenValue)) {
            try {
                return jwtTokenParser.parse(tokenValue, false).getPayload().getUserId();
            }
            catch (Exception ex) {
                return Throwables.whenNonFatal(
                        ex,
                        (CheckedFunction0<Optional<UUID>, RuntimeException>) Optional::empty
                );
            }
        }
        return Optional.empty();
    }

    private Optional<UUID> resolve(String username) {
        try {
            return Optional.ofNullable(userClient.findUserIdByUsername(username));
        }
        catch (FeignException.NotFound ex) {
            return Optional.empty();
        }
    }
}
