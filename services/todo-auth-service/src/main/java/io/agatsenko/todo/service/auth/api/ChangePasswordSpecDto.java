/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-29
 */
package io.agatsenko.todo.service.auth.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import io.agatsenko.todo.service.auth.service.ChenagePasswordSpec;

@ApiModel(value = "ChangePasswordSpec", description = "represents the specification for change user password")
@Getter
public class ChangePasswordSpecDto implements ChenagePasswordSpec {
    @ApiModelProperty(required = true)
    public final String oldPassword;

    @ApiModelProperty(required = true)
    public final String  newPassword;

    @ApiModelProperty(required = true)
    public final String confirmNewPassword;

    public ChangePasswordSpecDto(
            @JsonProperty("oldPassword") String oldPassword,
            @JsonProperty("newPassword") String newPassword,
            @JsonProperty("confirmNewPassword") String confirmNewPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }
}
