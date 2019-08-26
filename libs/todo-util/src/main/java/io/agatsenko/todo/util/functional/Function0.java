/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-25
 */
package io.agatsenko.todo.util.functional;

import io.agatsenko.todo.util.Check;

@FunctionalInterface
public interface Function0<R> {
    static <R> Function0<R> of(Function0<R> f) {
        return f;
    }

    R apply();

    default <R2> Function0<R2> andThen(Function1<? super R, ? extends R2> after) {
        Check.argNotNull(after, "after");
        return () -> after.apply(apply());
    }

    default CheckedFunction0<R, RuntimeException> toChecked() {
        return CheckedFunction0.of(this::apply);
    }
}
