/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-26
 */
package io.agatsenko.todo.util;

public class WrapFatalException extends RuntimeException {
    public WrapFatalException(Throwable cause) {
        super(cause);
    }
}
