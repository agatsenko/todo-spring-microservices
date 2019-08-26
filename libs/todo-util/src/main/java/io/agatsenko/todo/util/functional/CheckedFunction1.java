/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-25
 */
package io.agatsenko.todo.util.functional;

import io.agatsenko.todo.util.Check;

@FunctionalInterface
public interface CheckedFunction1<A1, R, E extends Throwable> {
    static <A1, R, E extends Throwable> CheckedFunction1<A1, R, E> of(CheckedFunction1<A1, R, E> f) {
        return f;
    }

    R apply(A1 a1) throws E;

    default <A0> CheckedFunction1<A0, R, E> compose(CheckedFunction1<? super A0, ? extends A1, ? extends E> before) {
        Check.argNotNull(before, "before");
        return (A0 a0) -> apply(before.apply(a0));
    }

    default <R2> CheckedFunction1<A1, R2, E> andThen(CheckedFunction1<? super R, ? extends R2, ? extends E> after) {
        Check.argNotNull(after, "after");
        return (A1 a1) -> after.apply(apply(a1));
    }
}
