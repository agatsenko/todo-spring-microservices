/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-20
 */
package io.agatsenko.todo.service.common.api;

import java.util.Map;

import org.springframework.web.context.request.WebRequest;

public class DefaultErrorAttributes extends org.springframework.boot.web.servlet.error.DefaultErrorAttributes {
    private static final String TIMESTAMP_ATTR = "timestamp";

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        final var attrs = super.getErrorAttributes(webRequest, includeStackTrace);
        attrs.remove("path");
        attrs.put(TIMESTAMP_ATTR, ApiError.currentTimestamp());
        return attrs;
    }
}
