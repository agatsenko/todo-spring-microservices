/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-24
 */
package io.agatsenko.todo.service.auth.service;

import javax.validation.constraints.NotBlank;

public interface ChenagePasswordSpec {
    @NotBlank
    String getOldPassword();

    @NotBlank
    String getNewPassword();

    @NotBlank
    String getConfirmNewPassword();
}
