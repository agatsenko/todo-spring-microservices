/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-24
 */
package io.agatsenko.todo.service.auth.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.agatsenko.todo.service.auth.web.api.UserResponseAssembler;
import io.agatsenko.todo.service.common.web.api.dto.DtoAssemblers;
import io.agatsenko.todo.service.common.web.api.error.ApiExceptionHandlers;
import io.agatsenko.todo.service.common.web.api.error.DefaultErrorAttributes;

@Configuration
public class WebConfig implements WebMvcConfigurer {
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
                new UserResponseAssembler()
        ));
    }

    // TODO: warn - enable cors for any request
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
}
