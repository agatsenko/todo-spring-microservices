/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-24
 */
package io.agatsenko.todo.service.auth.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.agatsenko.todo.service.auth.web.api.UserProfileDtoAssembler;
import io.agatsenko.todo.service.common.web.api.dto.DtoAssemblers;
import io.agatsenko.todo.service.common.web.api.error.DefaultErrorAttributes;

@Configuration
public class ApiConfig {
    @Bean
    public DefaultErrorAttributes defaultErrorAttributes() {
        return new DefaultErrorAttributes();
    }

    @Bean
    public DtoAssemblers dtoAssemblers() {
        return new DtoAssemblers(List.of(
                new UserProfileDtoAssembler()
        ));
    }
}
