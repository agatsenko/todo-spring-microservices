/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-29
 */
package io.agatsenko.todo.service.auth.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;

import io.agatsenko.todo.service.auth.service.UpdateUserSpec;

@ApiModel(value = "UpdateProfileSpec", description = "represents the specification for update user profile")
@Getter
@ToString
public class UpdateProfileSpecDto implements UpdateUserSpec {
    @ApiModelProperty(required = true)
    public final String email;

    @JsonCreator
    public UpdateProfileSpecDto(
            @JsonProperty("email") String email) {
        this.email = email;
    }
}
