/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-15
 */
package io.agatsenko.todo.service.auth.security.userdetails;

import org.springframework.security.core.userdetails.UserDetails;

import io.agatsenko.todo.service.auth.model.User;

public interface UserDetailsFactory {
    UserDetails create(User user);
}
