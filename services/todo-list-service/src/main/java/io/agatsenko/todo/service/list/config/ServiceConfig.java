/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.list.config;

import javax.validation.Validator;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.agatsenko.todo.service.auth.client.openfiegn.UserClient;
import io.agatsenko.todo.service.common.security.jwt.JwtTokenParser;
import io.agatsenko.todo.service.common.service.locator.Services;
import io.agatsenko.todo.service.common.service.locator.spring.SpringServiceLocator;
import io.agatsenko.todo.service.list.model.TaskListRepo;
import io.agatsenko.todo.service.list.service.DefaultUserIdResolver;
import io.agatsenko.todo.service.list.service.TaskListService;
import io.agatsenko.todo.service.list.service.UserIdResolver;

@Configuration
public class ServiceConfig {
    public ServiceConfig(ApplicationContext context) {
        Services.setLocator(new SpringServiceLocator(context));
    }

    @Bean
    public UserIdResolver userIdResolver(JwtTokenParser jwtTokenParser, UserClient userClient) {
        return new DefaultUserIdResolver(jwtTokenParser, userClient);
    }

    @Bean
    public TaskListService taskListService(TaskListRepo listRepo, Validator validator) {
        return new TaskListService(listRepo, validator);
    }
}
