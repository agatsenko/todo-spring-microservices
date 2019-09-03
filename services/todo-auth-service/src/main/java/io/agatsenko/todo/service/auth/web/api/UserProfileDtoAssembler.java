/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-30
 */
package io.agatsenko.todo.service.auth.web.api;

import io.agatsenko.todo.service.auth.model.User;
import io.agatsenko.todo.service.common.web.api.dto.BaseDtoAssembler;

public class UserProfileDtoAssembler extends BaseDtoAssembler<User, UserProfileDto> {
    public UserProfileDtoAssembler() {
        super(User.class, UserProfileDto.class);
    }

    @Override
    protected UserProfileDto assemblyNotNull(User model) {
        return UserProfileDto.builder()
                .name(model.getName())
                .email(model.getEmail())
                .build();
    }
}
