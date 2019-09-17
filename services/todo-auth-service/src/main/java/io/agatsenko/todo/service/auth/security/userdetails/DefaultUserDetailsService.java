/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-15
 */
package io.agatsenko.todo.service.auth.security.userdetails;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import io.agatsenko.todo.service.auth.model.UserRepo;

@RequiredArgsConstructor
public class DefaultUserDetailsService implements UserDetailsService {
    @NonNull
    private final UserRepo userRepo;
    @NonNull
    private final UserDetailsFactory userDetailsFactory;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo
                .findByUsername(username)
                .map(userDetailsFactory::create)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("'%s' user is not found", username)));
    }
}
