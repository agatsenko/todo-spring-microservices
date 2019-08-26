/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-24
 */
package io.agatsenko.todo.service.auth.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import io.agatsenko.todo.service.auth.service.NewUserSpec;

@ApiModel(value = "NewUserSpec", description = "represents the new user specification")
@Getter
@ToString
public class NewUserSpecDto implements NewUserSpec {
    @ApiModelProperty(required = true)
    public final String name;

    @ApiModelProperty(required = true)
    public final String email;

    @ApiModelProperty(required = true)
    public final String password;

    @ApiModelProperty(required = true)
    public final String confirmPassword;

    @JsonCreator
    @Builder
    public NewUserSpecDto(
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("confirmPassword") String confirmPassword) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
