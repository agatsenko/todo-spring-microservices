/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-30
 */
package io.agatsenko.todo.service.common.security;

import java.security.Principal;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(of = "userId")
@ToString(of = "name")
public class TodoUserPrincipal implements Principal {
    private final UUID userId;
    private final String name;
}
