/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-22
 */
package io.agatsenko.todo.service.auth.client.openfiegn;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserClientFallback implements UserClient {
    private static final Logger logger = LoggerFactory.getLogger(UserClientFallback.class);

    @Override
    public UUID findUserIdByUsername(String username) {
        logger.error("unable to find user id by '{}' username", username);
        return null;
    }
}
