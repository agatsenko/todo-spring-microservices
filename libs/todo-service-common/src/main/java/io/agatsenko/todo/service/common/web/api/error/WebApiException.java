/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.common.web.api.error;

import org.springframework.http.HttpStatus;

public class WebApiException extends RuntimeException {
    private final HttpStatus status;

    public WebApiException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status == null ? HttpStatus.BAD_REQUEST : status;
    }

    public WebApiException(HttpStatus status, String message) {
        this(status, message, null);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
