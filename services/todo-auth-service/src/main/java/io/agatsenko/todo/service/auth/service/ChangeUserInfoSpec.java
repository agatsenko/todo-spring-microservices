/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-24
 */
package io.agatsenko.todo.service.auth.service;

import java.util.UUID;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface ChangeUserInfoSpec {
    @NotNull
    UUID getUserId();

    @NotBlank
    String getName();

    @NotBlank
    @Email
    String getEmail();
}
