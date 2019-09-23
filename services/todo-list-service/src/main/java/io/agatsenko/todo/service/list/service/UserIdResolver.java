/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-22
 */
package io.agatsenko.todo.service.list.service;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

public interface UserIdResolver {
    Optional<UUID> resolve(Principal principal);
}
