/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-24
 */
package io.agatsenko.todo.service.auth.service;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public interface NewUserSpec {
    @NotBlank
    String getName();

    @NotBlank
    @Email
    String getEmail();

    @NotBlank
    String getPassword();

    @NotBlank
    String getConfirmPassword();
}
