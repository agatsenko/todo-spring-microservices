/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-17
 */
package io.agatsenko.todo.service.auth.web.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.agatsenko.todo.service.common.web.api.error.ApiError;

@ControllerAdvice
public class RestExceptionHandlers extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {
            IllegalArgumentException.class,
            IllegalStateException.class
    })
    public ResponseEntity<?> illegalArgAndStateExceptionHandler(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(
                ex,
                ApiError.badRequest(ex.getMessage()),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }
}
