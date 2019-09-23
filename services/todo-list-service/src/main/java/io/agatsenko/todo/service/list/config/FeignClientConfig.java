/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-23
 */
package io.agatsenko.todo.service.list.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.agatsenko.todo.service.auth.client.openfiegn.UserClientFallback;

@Configuration
public class FeignClientConfig {
    @Bean
    public UserClientFallback userClientFallback() {
        return new UserClientFallback();
    }
}
