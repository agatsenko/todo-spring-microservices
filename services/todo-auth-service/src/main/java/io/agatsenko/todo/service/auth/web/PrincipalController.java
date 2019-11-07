/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-17
 */
package io.agatsenko.todo.service.auth.web;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import io.agatsenko.todo.service.common.log.LogError;
import io.agatsenko.todo.service.common.log.LogInfo;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/principal/current")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PrincipalController {
    private static final List<String> removePrincipalFields = List.of(
            "password"
    );

    @NonNull
    private final ObjectMapper objMapper;

    @LogInfo
    @LogError
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    public Map getPrincipal(Principal principal) {
        return removeAllKeys(objMapper.convertValue(principal, Map.class), removePrincipalFields);
    }

    private static Map removeAllKeys(Map map, List<String> keys) {
        for (final var key : keys) {
            map.remove(key);
        }
        for (final var value : map.values()) {
            if (value instanceof Map) {
                removeAllKeys((Map) value, keys);
            }
        }
        return map;
    }
}
