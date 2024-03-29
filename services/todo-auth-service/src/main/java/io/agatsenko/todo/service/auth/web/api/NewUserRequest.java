/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-24
 */
package io.agatsenko.todo.service.auth.web.api;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

import io.agatsenko.todo.service.auth.model.UserRole;
import io.agatsenko.todo.service.auth.service.NewUserSpec;

@Value
public class NewUserRequest implements NewUserSpec {
    @ApiModelProperty(required = true)
    public final String username;

    @ApiModelProperty(required = true)
    public final String password;

    @ApiModelProperty(required = true)
    public final String confirmPassword;

    @ApiModelProperty(required = true)
    public final String email;

    @ApiModelProperty(required = true)
    public final boolean enabled;

    @ApiModelProperty(required = true)
    public Collection<UserRole> roles;

    @JsonCreator
    @Builder
    public NewUserRequest(
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("confirmPassword") String confirmPassword,
            @JsonProperty("email") String email,
            @JsonProperty("enabled") boolean enabled,
            @JsonProperty("roles") Collection<UserRole> roles) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
        this.enabled = enabled;
        this.roles = roles;
    }
}
