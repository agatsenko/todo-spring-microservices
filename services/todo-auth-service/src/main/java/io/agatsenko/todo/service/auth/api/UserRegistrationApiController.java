/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-24
 */
package io.agatsenko.todo.service.auth.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import io.agatsenko.todo.service.auth.service.UserService;
import io.agatsenko.todo.service.common.api.ApiError;
import io.agatsenko.todo.util.Check;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/api/user-registration")
public class UserRegistrationApiController {
    private final UserService userService;

    public UserRegistrationApiController(UserService userService) {
        Check.argNotNull(userService, "userService");
        this.userService = userService;
    }

    @ApiOperation(value = "registers the new user")
    @ApiResponses({
            @ApiResponse(
                    code = 204,
                    message = "user is successfully registered"
            ),
            @ApiResponse(
                    code = 400,
                    message = "new user specification is invalid",
                    response = ApiError.class
            ),
            @ApiResponse(
                    code = 500,
                    message = "internal server error",
                    response = ApiError.class
            ),
    })
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void registration(
            @ApiParam(name = "newUserSpec", value = "specification of the new user", required = true)
            @RequestBody NewUserSpecDto dto) {
        userService.registerNewUser(dto);
//        return ResponseEntity.noContent().build();
    }
}
