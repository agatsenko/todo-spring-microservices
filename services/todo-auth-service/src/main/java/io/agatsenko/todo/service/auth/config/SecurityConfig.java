/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-20
 */
package io.agatsenko.todo.service.auth.config;

import java.security.KeyPair;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import io.agatsenko.todo.service.auth.model.OAuth2TokenRepo;
import io.agatsenko.todo.service.auth.model.UserRepo;
import io.agatsenko.todo.service.common.security.jwt.*;
import io.agatsenko.todo.service.auth.security.oauth.MongoTokenServices;
import io.agatsenko.todo.service.auth.security.oauth.MongoTokenStore;
import io.agatsenko.todo.service.common.security.jwt.jjwt.JjwtTokenParser;
import io.agatsenko.todo.service.common.security.jwt.jjwt.JjwtTokenValueFactory;
import io.agatsenko.todo.service.auth.security.userdetails.DefaultUserDetailsService;
import io.agatsenko.todo.service.auth.security.userdetails.PreAuthenticationUserDetailsService;

@Configuration
public class SecurityConfig {
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
    public UserDetailsService userDetailsService(UserRepo userRepo) {
        return new DefaultUserDetailsService(userRepo);
    }

    @Bean
    public PreAuthenticationUserDetailsService preAuthenticationUserDetailsService(UserRepo userRepo) {
        return new PreAuthenticationUserDetailsService(userRepo);
    }

    @Bean
    public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider(
            PreAuthenticationUserDetailsService userDetailsService) {
        final var provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public JwtSigningKeys jwtSigningKeys(SecurityProperties props) {
        return new JwtSigningKeyPair(jwtKeyPair(props));
    }

    @Bean
    public JwtTokenValueFactory jwtTokenValueFactory(JwtSigningKeys signingKeys) {
        return new JjwtTokenValueFactory(signingKeys);
    }

    @Bean
    public JwtTokenSplitter jwtTokenSplitter() {
        return JwtTokenSplitter.defaultSplitter;
    }

    @Bean
    public JwtTokenParser jwtTokenParser(JwtTokenSplitter splitter, JwtSigningKeys signingKeys) {
        return new JjwtTokenParser(splitter, signingKeys);
    }

    @Bean
    public AuthenticationKeyGenerator authenticationKeyGenerator() {
        return new DefaultAuthenticationKeyGenerator();
    }

    @Bean
    public MongoTokenStore mongoTokenStore(
            JwtTokenParser tokenParser,
            AuthenticationKeyGenerator authKeyGenerator,
            OAuth2TokenRepo tokenRepo) {
        return new MongoTokenStore(tokenParser, authKeyGenerator, tokenRepo);
    }

    @Bean
    @Primary
    public MongoTokenServices tokenServices(
            SecurityProperties props,
            AuthenticationKeyGenerator authKeyGenerator,
            JwtTokenValueFactory jwtTokenValueFactory,
            MongoTokenStore mongoTokenStore,
            AuthenticationManager authManager,
            ClientDetailsService clientDetailsService) {
        return new MongoTokenServices(
                props.getOauth2().getAccessTokenValiditySeconds(),
                props.getOauth2().getRefreshTokenValiditySeconds(),
                authKeyGenerator,
                jwtTokenValueFactory,
                mongoTokenStore,
                authManager,
                clientDetailsService
        );
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
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
