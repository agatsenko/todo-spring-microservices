/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-17
 */
package io.agatsenko.todo.service.auth.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public UserDto createNewUser(NewUserSpecDto specDto) {
        // FIXME: not yet implemented
        throw new IllegalStateException("not yet implemented");
    }
}
