/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-17
 */
package io.agatsenko.todo.service.auth.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import lombok.Data;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import io.agatsenko.todo.service.auth.security.oauth.OAuthScope;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private static final String API_PACKAGE = "io.agatsenko.todo.service.auth.web.api";

    private static final String API_TITLE = "auth service api";
    private static final String API_DESCRIPTION = "rest api for access to the auth service";

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SwaggerProperties swaggerProperties() {
        return new SwaggerProperties();
    }

    @Bean
    public SecurityConfiguration securityConfiguration(
            SwaggerProperties swaggerProps,
            SecurityProperties securityProps) {
        final SecurityProperties.OAuthClientProperties clientProps = securityProps
                .getOauth2()
                .getClients()
                .get(swaggerProps.getSecurity().getDefaultClientId());
        return SecurityConfigurationBuilder.builder()
                .clientId(clientProps.getClientId())
                .clientSecret(clientProps.getDecodedSecret())
                .scopeSeparator(" ")
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }

    @Bean
    public Docket apiDocket(SwaggerProperties swaggerProps, SecurityProperties securityProps) {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage(API_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(List.of(securityScheme(swaggerProps, securityProps)))
                .securityContexts(List.of(securityContext()));
    }

    private static ApiInfo apiInfo() {
        return new ApiInfo(
                API_TITLE,
                API_DESCRIPTION,
                null,
                null,
                null,
                null,
                null,
                Collections.emptyList()
        );
    }

    private static AuthorizationScope[] scopes() {
        return new AuthorizationScope[] {
                new AuthorizationScope(OAuthScope.UI.getValue(), OAuthScope.UI.getDescription()),
                new AuthorizationScope(OAuthScope.SERVER.getValue(), OAuthScope.SERVER.getDescription())
        };
    }

    private static SecurityScheme securityScheme(SwaggerProperties swaggerProps, SecurityProperties securityProps) {
        final SecurityProperties.OAuthClientProperties clientProps = securityProps
                .getOauth2()
                .getClients()
                .get(swaggerProps.getSecurity().getDefaultClientId());
        final GrantType grantType1 = new ResourceOwnerPasswordCredentialsGrant(swaggerProps.getSecurity().getTokenEndpointUrl());
        return new OAuthBuilder()
                .name("oauth")
                .grantTypes(List.of(grantType1))
                .scopes(Arrays.asList(scopes()))
                .build();
    }

    private static SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(List.of(new SecurityReference("oauth", scopes())))
                .forPaths(PathSelectors.any())
                .build();
    }

    @ConfigurationProperties(prefix = "swagger")
    @Data
    public static class SwaggerProperties {
        private SwaggerSecurityProperties security;
    }

    @Data
    public static class SwaggerSecurityProperties {
        private String defaultClientId;
        private String tokenEndpointUrl;
        private String authorizeEndpointUrl;
    }
}
