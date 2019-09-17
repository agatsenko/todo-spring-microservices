/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-15
 */
package io.agatsenko.todo.service.auth.security.userdetails;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.agatsenko.todo.service.auth.model.User;
import io.agatsenko.todo.util.Check;

public class DefaultUserDetailsFactory implements UserDetailsFactory {
    @Override
    public UserDetails create(User user) {
        Check.argNotNull(user, "user");
        return new TodoUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                exposeAuthorities(user)
        );
    }

    private static Collection<? extends GrantedAuthority> exposeAuthorities(User user) {
        return user.getRoles().stream()
                .map(r -> "ROLE_" + r.name())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
