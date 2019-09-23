/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.common.service.locator;

public class ServiceLocatorException extends RuntimeException {
    public ServiceLocatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceLocatorException(String message) {
        super(message);
    }
}
