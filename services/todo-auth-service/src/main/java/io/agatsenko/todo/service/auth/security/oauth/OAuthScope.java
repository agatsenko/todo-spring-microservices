/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-15
 */
package io.agatsenko.todo.service.auth.security.oauth;

import lombok.experimental.UtilityClass;

public enum OAuthScope {
    SERVER(Value.SERVER, "internal server resources"),
    UI(Value.UI, "user interface resources"),
    ;

    private final String value;
    private final String description;

    OAuthScope(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return value;
    }

    @UtilityClass
    public static class Value {
        public static final String SERVER = "server";
        public static final String UI = "ui";
    }
}
