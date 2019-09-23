/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.list.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import io.agatsenko.todo.service.common.security.jwt.JwtSigningKeys;
import io.agatsenko.todo.service.common.security.jwt.JwtTokenParser;
import io.agatsenko.todo.service.common.security.jwt.JwtTokenSplitter;
import io.agatsenko.todo.service.common.security.jwt.jjwt.JjwtTokenParser;

@Configuration
public class SecurityConfig {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SecurityProperties securityProperties() {
        return new SecurityProperties();
    }

    @Bean
    public JwtTokenSplitter jwtTokenSplitter() {
        return JwtTokenSplitter.defaultSplitter;
    }

    @Bean
    public JwtSigningKeys jwtSigningKeys() {
        return JwtSigningKeys.empty;
    }

    @Bean
    public JwtTokenParser jwtTokenParser(JwtTokenSplitter splitter, JwtSigningKeys signingKeys) {
        return new JjwtTokenParser(splitter, signingKeys);
    }
}
