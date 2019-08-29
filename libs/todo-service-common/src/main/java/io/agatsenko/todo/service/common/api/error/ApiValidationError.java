/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-21
 */
package io.agatsenko.todo.service.common.api.error;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Singular;
import lombok.ToString;

import io.agatsenko.todo.util.Check;

@ApiModel(value = "ApiValidationError", description = "represents the validation error response")
@ToString
public class ApiValidationError extends ApiError {
    private static final String DEFAULT_ERROR_MSG = "validation error";

    public final List<Violation> violations;

    @JsonCreator
    private ApiValidationError(
            @JsonProperty("timestamp") ZonedDateTime timestamp,
            @JsonProperty("status") int status,
            @JsonProperty("error") String error,
            @JsonProperty("message") String message,
            @JsonProperty("violations") List<Violation> violations) {
        super(timestamp, status, error, message);
        this.violations = violations;
    }

    @Builder(builderMethodName = "validationErrorBuilder")
    public ApiValidationError(ZonedDateTime timestamp, String message, @Singular Collection<Violation> violations) {
        this(
                timestamp,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                toImmutableViolationList(violations)
        );
    }

    public ApiValidationError(String message, Collection<Violation> violations) {
        this(ZonedDateTime.now(), message, violations);
    }

    public ApiValidationError(Collection<Violation> violations) {
        this(ZonedDateTime.now(), DEFAULT_ERROR_MSG, violations);
    }

    public ApiValidationError(ZonedDateTime timestamp, ConstraintViolationException ex) {
        this(
                timestamp,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                DEFAULT_ERROR_MSG,
                toImmutableViolationList(ex)
        );
    }

    public ApiValidationError(ConstraintViolationException ex) {
        this(ZonedDateTime.now(), ex);
    }

    private static List<Violation> toImmutableViolationList(Collection<Violation> violations) {
        Check.argNotEmpty(violations, "violations");
        return Collections.unmodifiableList(violations.stream().collect(Collectors.toList()));
    }

    private static List<Violation> toImmutableViolationList(ConstraintViolationException ex) {
        Check.argNotNull(ex, "ex");
        Check.argNotEmpty(ex.getConstraintViolations(), "ex.constraintViolations");
        return Collections.unmodifiableList(
                ex.getConstraintViolations().stream()
                        .map(cv -> new Violation(cv.getMessage(), cv.getPropertyPath().toString()))
                        .collect(Collectors.toList())
        );
    }

    @ToString
    public static class Violation {
        public final String message;
        public final String path;

        @JsonCreator
        public Violation(
                @JsonProperty("message") String message,
                @JsonProperty("path") String path) {
            this.message = message;
            this.path = path;
        }
    }
}
