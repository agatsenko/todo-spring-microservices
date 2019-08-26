/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-25
 */
package io.agatsenko.todo.util.functional;

import io.agatsenko.todo.util.Check;

@FunctionalInterface
public interface CheckedFunction2<A1, A2, R, E extends Throwable> {
    static <A1, A2, R, E extends Throwable> CheckedFunction2<A1, A2, R, E> of(CheckedFunction2<A1, A2, R, E> f) {
        return f;
    }

    R apply(A1 a1, A2 a2) throws E;

    default <R2> CheckedFunction2<A1, A2, R2, E> andThen(CheckedFunction1<? super R, ? extends R2, ? extends E> after) {
        Check.argNotNull(after, "after");
        return (A1 a1, A2 a2) -> after.apply(apply(a1, a2));
    }
}
