/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-17
 */
package io.agatsenko.todo.service.auth.service;

import java.util.Collection;
import java.util.UUID;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.agatsenko.todo.service.auth.model.UserRole;

public interface UpdateUserSpec {
    @NotNull
    UUID getId();

    long getVersion();

    @NotBlank
    String getUsername();

    @NotBlank
    @Email
    String getEmail();

    boolean isEnabled();

    @NotNull
    @NotEmpty
    Collection<UserRole> getRoles();
}
