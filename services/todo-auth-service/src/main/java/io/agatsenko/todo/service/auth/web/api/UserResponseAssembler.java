/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-17
 */
package io.agatsenko.todo.service.auth.web.api;

import java.util.Collections;

import io.agatsenko.todo.service.auth.model.User;
import io.agatsenko.todo.service.common.web.api.dto.BaseDtoAssembler;

public class UserResponseAssembler extends BaseDtoAssembler<User, UserResponse> {
    public UserResponseAssembler() {
        super(User.class, UserResponse.class);
    }

    @Override
    protected UserResponse assemblyNotNull(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .version(user.getVersion())
                .username(user.getUsername())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .roles(Collections.unmodifiableCollection(user.getRoles()))
                .build();
    }
}
