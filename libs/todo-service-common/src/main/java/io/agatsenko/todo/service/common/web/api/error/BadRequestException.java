/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.common.web.api.error;

import org.springframework.http.HttpStatus;

public class BadRequestException extends WebApiException {
    public BadRequestException(String message, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, message, cause);
    }

    public BadRequestException(String message) {
        this(message, null);
    }
}
