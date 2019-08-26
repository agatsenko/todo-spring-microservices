/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-26
 */
package io.agatsenko.todo.util;

import lombok.experimental.UtilityClass;

import io.agatsenko.todo.util.functional.CheckedFunction0;
import io.agatsenko.todo.util.functional.CheckedProcedure0;

@UtilityClass
public class Throwables {
    public static boolean isFatal(Throwable t) {
        return t instanceof VirtualMachineError
                || t instanceof InterruptedException
                || t instanceof ThreadDeath
                || t instanceof WrapFatalException;
    }

    public static <R, E extends Throwable> R whenNonFatal(Throwable t, CheckedFunction0<R, E> action) throws E {
        if (isFatal(t)) {
            if (t instanceof Error) {
                throw (Error) t;
            }
            else if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            }
            else {
                throw new WrapFatalException(t);
            }
        }
        return action.apply();
    }

    public static <E extends Throwable> void whenNonFatal(Throwable t, CheckedProcedure0<E> action) throws E {
        whenNonFatal(t, () -> {
            action.apply();
            return null;
        });
    }
}
