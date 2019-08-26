/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-25
 */
package io.agatsenko.todo.util.functional;

import io.agatsenko.todo.util.Check;

@FunctionalInterface
public interface Function1<A1, R> {
    static <A1, R> Function1<A1, R> of(Function1<A1, R> f) {
        return f;
    }

    R apply(A1 a1);

    default <A0> Function1<A0, R> compose(Function1<? super A0, ? extends A1> before) {
        Check.argNotNull(before, "before");
        return (A0 a0) -> apply(before.apply(a0));
    }

    default <R2> Function1<A1, R2> andThen(Function1<? super R, ? extends R2> after) {
        Check.argNotNull(after, "after");
        return (A1 a11) -> after.apply(apply(a11));
    }

    default CheckedFunction1<A1, R, RuntimeException> toChecked() {
        return CheckedFunction1.of(this::apply);
    }
}
