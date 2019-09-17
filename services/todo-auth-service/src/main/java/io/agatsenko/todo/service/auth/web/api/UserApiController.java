/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-17
 */
package io.agatsenko.todo.service.auth.web.api;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import io.agatsenko.todo.service.auth.model.User;
import io.agatsenko.todo.service.auth.security.oauth.OAuthScope;
import io.agatsenko.todo.service.auth.service.UserService;
import io.agatsenko.todo.service.common.web.api.dto.DtoAssemblers;
import io.agatsenko.todo.service.common.web.api.error.ApiError;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@PreAuthorize("#oauth2.hasScope('" + OAuthScope.Value.SERVER + "')")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserApiController {
    private final UserService userService;
    private final DtoAssemblers dtoAssemblers;

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getUser(@PathVariable("id") UUID userId) {
        return userService.getUser(userId)
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(dtoAssemblers.assembly(UserResponse.class, user)))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ApiError.notFound("user with id=%s is not found", userId))
                );
    }

    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    public List<UserResponse> getAllUsers() {
        return dtoAssemblers.assembly(User.class, UserResponse.class, userService.getAllUsers());
    }

    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public UserResponse createUser(@RequestBody NewUserRequest request) {
        return dtoAssemblers.assembly(UserResponse.class, userService.createUser(request));
    }

    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest request) {
        return userService.updateUser(request)
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(dtoAssemblers.assembly(UserResponse.class, user)))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ApiError.notFound(
                                "user with id=%s and version=%s is not found",
                                request.getId(),
                                request.getVersion()
                        ))
                );
    }

    @PutMapping(
            value = "/{id}/password",
            consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<?> changeUserPassword(
            @PathVariable("id") UUID userId,
            @RequestBody ChangePasswordRequest request) {
        return userService.changePassword(userId, request)
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(dtoAssemblers.assembly(UserResponse.class, user)))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ApiError.notFound("user with id=%s is not found", userId))
                );
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") UUID userId) {
        return userService.deleteUser(userId) ?
                (ResponseEntity<?>) ResponseEntity.noContent().build() :
                ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ApiError.notFound("user with id=%s is not found", userId));
    }
}
