/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-23
 */
package io.agatsenko.todo.service.list.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.agatsenko.todo.service.common.web.api.dto.DtoAssemblers;
import io.agatsenko.todo.service.common.web.api.error.ApiExceptionHandlers;
import io.agatsenko.todo.service.common.web.api.error.DefaultErrorAttributes;
import io.agatsenko.todo.service.list.web.api.TaskListResponseAssembler;
import io.agatsenko.todo.service.list.web.api.TaskResponseAssembler;

@Configuration
public class WebConfig {
    @Bean
    public DefaultErrorAttributes defaultErrorAttributes() {
        return new DefaultErrorAttributes();
    }

    @Bean
    public ApiExceptionHandlers apiExceptionHandlers() {
        return new ApiExceptionHandlers();
    }

    @Bean
    public DtoAssemblers dtoAssemblers() {
        return new DtoAssemblers(List.of(
                new TaskResponseAssembler(),
                new TaskListResponseAssembler()
        ));
    }
}
