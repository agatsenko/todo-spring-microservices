/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-20
 */
package io.agatsenko.todo.service.common.api.error;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.WebRequest;
import com.google.common.base.Strings;

public class DefaultErrorAttributes extends org.springframework.boot.web.servlet.error.DefaultErrorAttributes {
    private static final String TIMESTAMP_ATTR = "timestamp";
    private static final String MESSAGE_ATTR = "message";
    private static final String PATH_ATTR = "path";

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        final var attrs = super.getErrorAttributes(webRequest, includeStackTrace);

        attrs.remove(MESSAGE_ATTR);
        attrs.remove(PATH_ATTR);

        final var contentType = webRequest.getHeader(HttpHeaders.CONTENT_TYPE);
        if (!Strings.isNullOrEmpty(contentType)) {
            final var mediaType = MediaType.parseMediaType(contentType).getType();
            if (MediaType.APPLICATION_JSON_VALUE.equals(mediaType)) {
                attrs.put(TIMESTAMP_ATTR, ApiError.currentTimestamp());
            }
        }

        return attrs;
    }
}
