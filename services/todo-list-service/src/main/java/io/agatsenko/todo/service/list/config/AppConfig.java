/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.list.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.agatsenko.todo.service.common.log.LogAspects;

@Configuration
public class AppConfig {
    @Bean
    public LogAspects logAspects() {
        return new LogAspects();
    }
}
