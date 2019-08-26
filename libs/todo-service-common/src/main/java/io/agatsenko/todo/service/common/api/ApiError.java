/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-24
 */
package io.agatsenko.todo.service.common.api;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.ToString;

@ApiModel(value = "ApiError", description = "represents the api error response")
@ToString
public class ApiError {
    public final ZonedDateTime time;
    public final int status;
    public final String error;
    public final String message;

    @JsonCreator
    @Builder(builderMethodName = "errorBuilder")
    public ApiError(
            @JsonProperty("timestamp") ZonedDateTime time,
            @JsonProperty("status") int status,
            @JsonProperty("error") String error,
            @JsonProperty("message") String message) {
        this.time = time;
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public ApiError(int status, String error, String message) {
        this(currentTimestamp(), status, error, message);
    }

    public static ZonedDateTime currentTimestamp() {
        return ZonedDateTime.now();
    }

    public static ApiError notFound(String message) {
        return new ApiError(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                message
        );
    }

    public static ApiError notFound(String messageTemplate, Object ... args) {
        return notFound(String.format(messageTemplate, args));
    }

    public static ApiError badRequest(String message) {
        return new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message
        );
    }
}
