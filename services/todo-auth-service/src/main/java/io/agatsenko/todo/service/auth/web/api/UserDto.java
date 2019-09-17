/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-17
 */
package io.agatsenko.todo.service.auth.web.api;

import java.util.Collection;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import io.agatsenko.todo.service.auth.model.UserRole;

@Value
public class UserDto {
    @ApiModelProperty(required = true)
    public final UUID id;

    @ApiModelProperty
    public final Long version;

    @ApiModelProperty(required = true)
    public final String username;

    @ApiModelProperty(required = true)
    public final String email;

    @ApiModelProperty(required = true)
    public final boolean enabled;

    @ApiModelProperty(required = true)
    public final Collection<UserRole> roles;

    @Builder
    public UserDto(
            @JsonProperty("id") UUID id,
            @JsonProperty("version") Long version,
            @JsonProperty("username") String username,
            @JsonProperty("email") String email,
            @JsonProperty("enabled") boolean enabled,
            @JsonProperty("roles") @Singular Collection<UserRole> roles) {
        this.id = id;
        this.version = version;
        this.username = username;
        this.email = email;
        this.enabled = enabled;
        this.roles = roles;
    }
}
