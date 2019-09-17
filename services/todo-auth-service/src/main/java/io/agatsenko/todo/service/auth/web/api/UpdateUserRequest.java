/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-17
 */
package io.agatsenko.todo.service.auth.web.api;

import java.util.Collection;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

import io.agatsenko.todo.service.auth.model.UserRole;
import io.agatsenko.todo.service.auth.service.UpdateUserSpec;

@Value
public class UpdateUserRequest implements UpdateUserSpec {
    @ApiModelProperty(required = true)
    public final UUID id;

    @ApiModelProperty(required = true)
    public final long version;

    @ApiModelProperty(required = true)
    public final String username;

    @ApiModelProperty(required = true)
    public final String email;

    @ApiModelProperty(required = true)
    public final boolean enabled;

    @ApiModelProperty(required = true)
    public final Collection<UserRole> roles;

    @JsonCreator
    @Builder
    public UpdateUserRequest(
            @JsonProperty("id") UUID id,
            @JsonProperty("version") long version,
            @JsonProperty("username") String username,
            @JsonProperty("email") String email,
            @JsonProperty("enabled") boolean enabled,
            @JsonProperty("roles") Collection<UserRole> roles) {
        this.id = id;
        this.version = version;
        this.username = username;
        this.email = email;
        this.enabled = enabled;
        this.roles = roles;
    }
}
