/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-20
 */
package io.agatsenko.todo.service.auth.security.userdetails;

import java.security.Principal;

import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import io.agatsenko.todo.service.auth.model.User;
import io.agatsenko.todo.service.auth.model.UserRepo;

@RequiredArgsConstructor
public class PreAuthenticationUserDetailsService
        implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    @NonNull
    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        UserDetails userDetails = null;
        if (token.getPrincipal() instanceof User) {
            userDetails = userRepo.findById(((User) token.getPrincipal()).getId()).orElse(null);
        }
        if (token.getPrincipal() instanceof Principal) {
            userDetails = userRepo.findByUsername(((Principal) token.getPrincipal()).getName()).orElse(null);
        }
        if (userDetails == null) {
            throw new UsernameNotFoundException("unable to find UserDetails for " + token);
        }
        return userDetails;
    }
}
