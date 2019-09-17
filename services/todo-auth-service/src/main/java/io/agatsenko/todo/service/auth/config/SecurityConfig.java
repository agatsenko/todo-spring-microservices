/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-15
 */
package io.agatsenko.todo.service.auth.config;

import java.security.KeyPair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import lombok.RequiredArgsConstructor;

import io.agatsenko.todo.service.auth.model.UserRepo;
import io.agatsenko.todo.service.auth.security.userdetails.DefaultUserDetailsFactory;
import io.agatsenko.todo.service.auth.security.userdetails.DefaultUserDetailsService;
import io.agatsenko.todo.service.auth.security.userdetails.UserDetailsFactory;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SecurityProperties securityProperties() {
        return new SecurityProperties();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsFactory userDetailsFactory() {
        return new DefaultUserDetailsFactory();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepo userRepo, UserDetailsFactory userDetailsFactory) {
        return new DefaultUserDetailsService(userRepo, userDetailsFactory);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(SecurityProperties securityProps) {
        final var tokenConverter = new JwtAccessTokenConverter();
        tokenConverter.setKeyPair(jwtKeyPair(securityProps));
        return tokenConverter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices(
            SecurityProperties securityProps,
            JwtAccessTokenConverter tokenConverter,
            TokenStore tokenStore) {
        final var services = new DefaultTokenServices();
        services.setTokenStore(tokenStore);
        services.setSupportRefreshToken(true);
        services.setAccessTokenValiditySeconds(securityProps.getOauth2().getAccessTokenActiveSeconds());
        services.setRefreshTokenValiditySeconds(securityProps.getOauth2().getRefreshTokenActiveSeconds());
        services.setTokenEnhancer(tokenConverter);
        return services;
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }

    @Override
    public void configure(WebSecurity web) {
        final var secProps = securityProperties();
        web
                .ignoring()
                .antMatchers(secProps.getWhitelistUrlPatterns())
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated()
        ;
    }

    private KeyPair jwtKeyPair(SecurityProperties props) {
        final var factory = new KeyStoreKeyFactory(
                props.getOauth2().getJwt().getKeyStore(),
                props.getOauth2().getJwt().getKeyStorePassword()
        );
        return factory.getKeyPair(
                props.getOauth2().getJwt().getKeyPairAlias(),
                props.getOauth2().getJwt().getKeyPairPassword()
        );
    }
}
