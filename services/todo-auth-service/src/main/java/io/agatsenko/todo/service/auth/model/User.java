/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-24
 */
package io.agatsenko.todo.service.auth.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.*;

@Document("users")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = { "id", "version", "username", "email", "enabled", "roles" })
public class User implements UserDetails {
    @NotNull
    @Id
    private final UUID id;

    @Version
    private long version;

    @NotBlank
    private final String username;

    @NotBlank
    private final String password;

    @NotBlank
    @Email
    private final String email;

    private final boolean enabled;

    @NotNull
    @NotEmpty
    private final Set<UserRole> roles;

    @Transient
    private final Collection<? extends GrantedAuthority> authorities;

    @Builder(toBuilder = true)
    public User(
            UUID id,
            long version,
            String username,
            String password,
            String email,
            boolean enabled,
            @Singular Set<UserRole> roles) {
        this.id = id;
        this.version = version;
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.roles = roles == null ? null : Set.copyOf(roles);
        this.authorities = toAuthorities(roles);
    }

    private static Collection<? extends GrantedAuthority> toAuthorities(Set<UserRole> roles) {
        if (roles == null) {
            return Collections.emptyList();
        }
        return roles.stream()
                .map(r -> "ROLE_" + r.name())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
