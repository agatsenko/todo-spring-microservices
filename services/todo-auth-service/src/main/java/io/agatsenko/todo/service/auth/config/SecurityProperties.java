/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-15
 */
package io.agatsenko.todo.service.auth.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import com.google.common.base.Strings;
import lombok.Data;

@ConfigurationProperties(prefix = "security")
@Data
public class SecurityProperties {
    private OauthProperties oauth2;
    private String[] whitelistUrlPatterns;

    public SecurityProperties apply(ClientDetailsServiceBuilder builder) {
        if (oauth2 != null && oauth2.getClients() != null) {
            for (final var clientProps : oauth2.getClients().values()) {
                if (clientProps != null) {
                    clientProps.apply(builder);
                }
            }
        }
        return this;
    }

    @Data
    public static class OauthProperties {
        private int accessTokenValiditySeconds;
        private int refreshTokenValiditySeconds;
        private Map<String, OAuthClientProperties> clients;
        private JwtProperties jwt;
    }

    @Data
    public static class JwtProperties {
        private Resource keyStore;
        private char[] keyStorePassword;
        private String keyPairAlias;
        private char[] keyPairPassword;
    }

    @Data
    public static class OAuthClientProperties {
        private String clientId;
        private String encodedSecret;
        private String decodedSecret;
        private String[] scope;
        private String[] authorizedGrantTypes;
        private String[] authorities;

        public void apply(ClientDetailsServiceBuilder builder) {
            final var client = builder.withClient(clientId);
            if (!Strings.isNullOrEmpty(encodedSecret)) {
                client.secret(encodedSecret);
            }
            if (scope != null && scope.length > 0) {
                client.scopes(scope);
            }
            if (authorizedGrantTypes != null && authorizedGrantTypes.length > 0) {
                client.authorizedGrantTypes(authorizedGrantTypes);
            }
            if (authorities != null && authorities.length > 0) {
                client.authorities(authorities);
            }
        }
    }
}
