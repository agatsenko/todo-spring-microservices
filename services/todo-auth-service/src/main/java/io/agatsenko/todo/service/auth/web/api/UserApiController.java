/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-17
 */
package io.agatsenko.todo.service.auth.web.api;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import io.agatsenko.todo.service.auth.client.contract.UserContract;
import io.agatsenko.todo.service.auth.model.User;
import io.agatsenko.todo.service.auth.security.oauth.OAuthScope;
import io.agatsenko.todo.service.auth.service.UserService;
import io.agatsenko.todo.service.common.log.LogError;
import io.agatsenko.todo.service.common.log.LogInfo;
import io.agatsenko.todo.service.common.web.api.dto.DtoAssemblers;
import io.agatsenko.todo.service.common.web.api.error.NotFoundException;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@PreAuthorize("#oauth2.hasScope('" + OAuthScope.Value.SERVER + "')")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserApiController implements UserContract {
    private final UserService userService;
    private final DtoAssemblers dtoAssemblers;

    @LogInfo
    @LogError
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public UserResponse getUser(@PathVariable("id") UUID userId) {
        return userService.getUser(userId)
                .map(user -> dtoAssemblers.assembly(UserResponse.class, user))
                .orElseThrow(() -> new NotFoundException(String.format("user with id=%s is not found", userId)));
    }

    @LogInfo
    @LogError
    @GetMapping(value = "/id", produces = APPLICATION_JSON_UTF8_VALUE)
    @Override
    public UUID findUserIdByUsername(@RequestParam(name = "username") String username) {
        return userService.getUser(username)
                .map(User::getId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("user with username=%s is not found", username)
                ));
    }

    @LogInfo
    @LogError
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    public List<UserResponse> getAllUsers() {
        return dtoAssemblers.assembly(User.class, UserResponse.class, userService.getAllUsers());
    }

    @LogInfo
    @LogError
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public UserResponse createUser(@RequestBody NewUserRequest request) {
        return dtoAssemblers.assembly(UserResponse.class, userService.createUser(request));
    }

    @LogInfo
    @LogError
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public UserResponse updateUser(@RequestBody UpdateUserRequest request) {
        return userService.updateUser(request)
                .map(user -> dtoAssemblers.assembly(UserResponse.class, user))
                .orElseThrow(() -> new NotFoundException(String.format(
                        "user with id=%s and version=%s is not found",
                        request.getId(),
                        request.getVersion()
                )));
    }

    @LogInfo
    @LogError
    @PutMapping(
            value = "/{id}/password",
            consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public UserResponse changeUserPassword(
            @PathVariable("id") UUID userId,
            @RequestBody ChangePasswordRequest request) {
        return userService.changePassword(userId, request)
                .map(user -> dtoAssemblers.assembly(UserResponse.class, user))
                .orElseThrow(() -> new NotFoundException(String.format("user with id=%s is not found", userId)));
    }

    @LogInfo
    @LogError
    @DeleteMapping(value = "{id}")
    public void deleteUser(@PathVariable("id") UUID userId) {
        if (!userService.deleteUser(userId)) {
            throw new NotFoundException(String.format("user with id=%s is not found", userId));
        }
    }
}
