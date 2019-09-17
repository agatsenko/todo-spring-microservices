/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-17
 */
package io.agatsenko.todo.service.auth.web.api;

import java.util.Collection;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import io.agatsenko.todo.service.auth.model.UserRole;

@Value
public class UserDto {
    public final UUID id;
    public final String username;
    public final String email;
    public final boolean enabled;
    public final Collection<UserRole> roles;

    @Builder
    public UserDto(
            @JsonProperty("id") UUID id,
            @JsonProperty("username") String username,
            @JsonProperty("email") String email,
            @JsonProperty("enabled") boolean enabled,
            @JsonProperty("roles") @Singular Collection<UserRole> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.enabled = enabled;
        this.roles = roles;
    }
}
