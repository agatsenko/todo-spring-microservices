/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-29
 */
package io.agatsenko.todo.service.auth.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import io.agatsenko.todo.service.common.api.error.ApiError;
import io.agatsenko.todo.service.common.api.error.ApiValidationError;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

// FIXME: need to add authentication error description (@ApiResponses)
@RestController
@RequestMapping("/api/profile")
public class ProfileApiController {
    @ApiOperation(value = "gets the user profile")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "user profile is found",
                    response = ProfileDto.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "internal server error",
                    response = ApiError.class
            ),
    })
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getProfile() {
        // FIXME: not yet implemented
        throw new IllegalStateException("not yet implemented");
    }

    @ApiOperation(value = "updates the user profile")
    @ApiResponses({
            @ApiResponse(
                    code = 204,
                    message = "user profile is successfully updated"
            ),
            @ApiResponse(
                    code = 400,
                    message = "specification is invalid",
                    response = ApiValidationError.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "internal server error",
                    response = ApiError.class
            ),
    })
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProfile(
            @ApiParam(name = "updateProfileSpec", value = "specification for update the user profile", required = true)
            @RequestBody UpdateProfileSpecDto specDto) {
        // FIXME: not yet implemented
        throw new IllegalStateException("not yet implemented");
    }

    @ApiOperation(value = "changes the user password")
    @ApiResponses({
            @ApiResponse(
                    code = 204,
                    message = "user profile is successfully updated"
            ),
            @ApiResponse(
                    code = 400,
                    message = "specification is invalid",
                    response = ApiValidationError.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "internal server error",
                    response = ApiError.class
            ),
    })
    @PutMapping(value = "/password", consumes = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            @ApiParam(
                    name = "changePasswordSpec",
                    value = "specification for change the user password",
                    required = true
            )
            @RequestBody ChangePasswordSpecDto changePasswordSpecDto) {
        // FIXME: not yet implemented
        throw new IllegalStateException("not yet implemented");
    }

    @ApiOperation(value = "deletes the account")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount() {
        // FIXME: not yet implemented
        throw new IllegalStateException("not yet implemented");
    }
}
