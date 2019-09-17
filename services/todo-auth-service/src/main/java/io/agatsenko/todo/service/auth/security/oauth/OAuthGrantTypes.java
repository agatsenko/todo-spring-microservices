/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-15
 */
package io.agatsenko.todo.service.auth.security.oauth;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OAuthGrantTypes {
    public static final String AUTHORIZATION_CODE = "authorization_code";
    public static final String PASSWORD = "password";
    public static final String CLIENT_CREDENTIALS = "client_credentials";
    public static final String IMPLICIT = "implicit";
    public static final String REFRESH_TOKEN = "refresh_token";
}
