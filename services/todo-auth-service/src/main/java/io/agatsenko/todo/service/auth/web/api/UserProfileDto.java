/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-29
 */
package io.agatsenko.todo.service.auth.web.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import io.agatsenko.todo.service.auth.service.UpdateUserSpec;

@ApiModel(value = "Profile", description = "represents the user profile")
@Getter
@ToString
public class UserProfileDto implements UpdateUserSpec {
    @ApiModelProperty(required = true)
    public final String name;

    @ApiModelProperty(required = true)
    public final String email;

    @Builder
    public UserProfileDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
