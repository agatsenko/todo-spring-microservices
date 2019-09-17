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

import io.agatsenko.todo.service.auth.security.oauth.OAuthScope;
import io.agatsenko.todo.service.common.web.api.dto.DtoAssemblers;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@PreAuthorize("#oauth2.hasScope('" + OAuthScope.Value.SERVER + "')")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserApiController {
    private final DtoAssemblers dtoAssemblers;

    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    public List<UserDto> getAllUsers() {
        // FIXME: not yet implemented
        throw new IllegalStateException("not yet implemented");
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public UserDto getUser(@PathVariable("id") UUID id) {
        // FIXME: not yet implemented
        throw new IllegalStateException("not yet implemented");
    }

    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public UserDto createUser(NewUserSpecDto specDto) {
        // FIXME: not yet implemented
        throw new IllegalStateException("not yet implemented");
    }

    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public UserDto updateUser(@RequestBody UserDto userDto) {
        // FIXME: not yet implemented
        throw new IllegalStateException("not yet implemented");
    }

    @DeleteMapping(value = "{id}")
    public void deleteUser(@PathVariable("id") UUID id) {
        // FIXME: not yet implemented
        throw new IllegalStateException("not yet implemented");
    }
}
