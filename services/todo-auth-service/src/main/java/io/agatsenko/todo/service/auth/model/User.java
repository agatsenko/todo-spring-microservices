/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-24
 */
package io.agatsenko.todo.service.auth.model;

import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

@Document("users")
@Value
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "username", "email", "enabled", "roles"})
public class User {
    @NotNull
    private final UUID id;

    @NotBlank
    private final String username;

    @NotBlank
    private final String password;

    @NotBlank
    @Email
    private final String email;

    private final boolean enabled;

    @Singular
    private final Set<UserRole> roles;
}
