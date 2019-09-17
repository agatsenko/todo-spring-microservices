/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-17
 */
package io.agatsenko.todo.service.auth.web.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

import io.agatsenko.todo.service.auth.service.ChangePasswordSpec;

@Value
public class ChangePasswordRequest implements ChangePasswordSpec {
    @ApiModelProperty(required = true)
    public final String oldPassword;

    @ApiModelProperty(required = true)
    public final String newPassword;

    @ApiModelProperty(required = true)
    public final String confirmNewPassword;

    @JsonCreator
    @Builder
    public ChangePasswordRequest(
            @JsonProperty("oldPassword") String oldPassword,
            @JsonProperty("newPassword") String newPassword,
            @JsonProperty("confirmNewPassword") String confirmNewPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }
}
