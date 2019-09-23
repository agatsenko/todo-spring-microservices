/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-22
 */
package io.agatsenko.todo.service.auth.client.contract;

import java.util.UUID;

public interface UserContract {
    UUID findUserIdByUsername(String username);
}
