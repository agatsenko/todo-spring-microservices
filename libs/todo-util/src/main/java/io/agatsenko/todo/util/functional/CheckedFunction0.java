/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-25
 */
package io.agatsenko.todo.util.functional;

import io.agatsenko.todo.util.Check;

@FunctionalInterface
public interface CheckedFunction0<R, E extends Throwable> {
    static <R, E extends Throwable> CheckedFunction0<R, E> of(CheckedFunction0<R, E> f) {
        return f;
    }

    R apply() throws E;

    default <R2> CheckedFunction0<R2, E> andThen(CheckedFunction1<? super R, ? extends R2, ? extends E> after) {
        Check.argNotNull(after, "after");
        return () -> after.apply(apply());
    }
}
