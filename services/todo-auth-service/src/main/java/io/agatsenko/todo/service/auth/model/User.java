/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-24
 */
package io.agatsenko.todo.service.auth.model;

import java.util.UUID;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Builder;
import lombok.Value;

@Document("users")
@Value
@Builder(toBuilder = true)
public class User {
    @NotNull
    private final UUID id;

    @NotBlank
    private final String name;

    @NotBlank
    @Email
    private final String email;

    @NotBlank
    private final String passwordHash;
}
