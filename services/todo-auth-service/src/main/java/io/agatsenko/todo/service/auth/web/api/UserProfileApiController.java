/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-29
 */
package io.agatsenko.todo.service.auth.web.api;

import java.util.UUID;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import io.agatsenko.todo.service.auth.service.UserService;
import io.agatsenko.todo.service.common.web.api.dto.DtoAssemblers;
import io.agatsenko.todo.service.common.web.api.error.ApiError;
import io.agatsenko.todo.service.common.web.api.error.ApiValidationError;
import io.agatsenko.todo.service.common.security.TodoUserPrincipal;
import io.agatsenko.todo.util.Check;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

// FIXME: need to add authentication error description (@ApiResponses)
@RestController
@RequestMapping("/api/profile")
public class UserProfileApiController {
    // FIXME: need to remove
    private static final TodoUserPrincipal userPrincipal = new TodoUserPrincipal(
            UUID.fromString("f7630005-9342-489d-a8ca-aaa25b2ad35d"),
            "tester"
    );

    private final UserService userService;
    private final DtoAssemblers dtoAssemblers;

    public UserProfileApiController(UserService userService, DtoAssemblers dtoAssemblers) {
        Check.argNotNull(userService, "userService");
        Check.argNotNull(dtoAssemblers, "dtoAssemblers");
        this.userService = userService;
        this.dtoAssemblers = dtoAssemblers;
    }

    @ApiOperation(value = "gets a user profile")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "the user profile is found",
                    response = UserProfileDto.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "the user profile is not found",
                    response = ApiValidationError.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "internal server error",
                    response = ApiError.class
            ),
    })
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getUserProfile() {
        return userService
                .getUser(userPrincipal.getUserId())
                .map(user -> dtoAssemblers.assembly(UserProfileDto.class, user))
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiError.notFound("user is not found"))
                );
    }

    @ApiOperation(value = "updates a user profile")
    @ApiResponses({
            @ApiResponse(
                    code = 204,
                    message = "the user profile is successfully updated"
            ),
            @ApiResponse(
                    code = 400,
                    message = "the specification is invalid",
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
            @Valid
            @RequestBody UpdateUserProfileSpecDto specDto) {
        // FIXME: not yet implemented
        throw new IllegalStateException("not yet implemented");
    }

    @ApiOperation(value = "changes a user password")
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
            @Valid
            @RequestBody ChangeUserPasswordSpecDto changeUserPasswordSpecDto) {
        // FIXME: not yet implemented
        throw new IllegalStateException("not yet implemented");
    }

    // FIXME: need to remove
    private static final Logger logger = LoggerFactory.getLogger(UserProfileApiController.class);

    @ApiOperation(value = "deletes an account")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount() {
        // FIXME: not yet implemented
        throw new IllegalStateException("not yet implemented");
    }
}
