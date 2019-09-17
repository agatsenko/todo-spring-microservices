/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-17
 */
package io.agatsenko.todo.service.auth.web.api;

import java.util.Collections;

import io.agatsenko.todo.service.auth.model.User;
import io.agatsenko.todo.service.common.web.api.dto.BaseDtoAssembler;

public class UserDtoAssembler extends BaseDtoAssembler<User, UserDto> {
    public UserDtoAssembler() {
        super(User.class, UserDto.class);
    }

    @Override
    protected UserDto assemblyNotNull(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .roles(Collections.unmodifiableCollection(user.getRoles()))
                .build();
    }
}
