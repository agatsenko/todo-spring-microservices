/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-24
 */
package io.agatsenko.todo.service.auth.service;

import java.util.Collection;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.agatsenko.todo.service.auth.model.UserRole;

public interface NewUserSpec {
    @NotBlank
    String getUsername();

    @NotBlank
    @Email
    String getEmail();

    @NotBlank
    String getPassword();

    @NotBlank
    String getConfirmPassword();

    boolean isEnabled();

    @NotNull
    @NotEmpty
    Collection<UserRole> getRoles();
}
