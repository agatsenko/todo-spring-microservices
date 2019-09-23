/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-22
 */
package io.agatsenko.todo.service.auth.client.openfiegn;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.agatsenko.todo.service.auth.client.contract.UserContract;
import io.agatsenko.todo.service.common.log.LogInfo;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@FeignClient(name = "todo-auth-service", fallback = UserClientFallback.class)
public interface UserClient extends UserContract {
    @LogInfo
    @GetMapping(value = "/uaa/api/users/id", produces = APPLICATION_JSON_UTF8_VALUE)
    @Override
    UUID findUserIdByUsername(@RequestParam(name = "username") String username);
}
