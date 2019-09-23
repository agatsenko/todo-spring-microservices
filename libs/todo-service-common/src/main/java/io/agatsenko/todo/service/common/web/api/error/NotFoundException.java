/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.common.web.api.error;

import org.springframework.http.HttpStatus;

public class NotFoundException extends WebApiException {
    public NotFoundException(String message, Throwable cause) {
        super(HttpStatus.NOT_FOUND, message, cause);
    }

    public NotFoundException(String message) {
        this(message, null);
    }
}
