/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-24
 */
package io.agatsenko.todo.service.auth.config;

import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.agatsenko.todo.service.auth.model.UserRepo;
import io.agatsenko.todo.service.auth.service.UserService;

@Configuration
public class ServiceConfig {
    @Bean
    public UserService userService(Validator validator, PasswordEncoder passwordEncoder, UserRepo userRepo) {
        return new UserService(validator, passwordEncoder, userRepo);
    }
}
