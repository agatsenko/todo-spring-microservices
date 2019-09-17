/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-24
 */
package io.agatsenko.todo.service.auth.service;

import java.util.Collection;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import io.agatsenko.todo.service.auth.model.UserRole;

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

    boolean isEnabled();

    Collection<UserRole> getRoles();
}
