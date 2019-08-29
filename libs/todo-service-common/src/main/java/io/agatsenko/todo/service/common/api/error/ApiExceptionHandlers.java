/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-26
 */
package io.agatsenko.todo.service.common.api.error;

import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ApiExceptionHandlers {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiValidationError> handleConstraintViolationException(
            ConstraintViolationException ex,
            WebRequest request) {
        return ResponseEntity.badRequest().body(new ApiValidationError(ex));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiValidationError> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        final var violations = ex.getBindingResult().getAllErrors().stream()
                .map(err -> {
                    if (err instanceof FieldError) {
                        final var fieldErr = (FieldError) err;
                        return new ApiValidationError.Violation(fieldErr.getDefaultMessage(), fieldErr.getField());
                    }
                    else {
                        return new ApiValidationError.Violation(err.getDefaultMessage(), null);
                    }
                })
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(new ApiValidationError(ex.getMessage(), violations));
    }
}
