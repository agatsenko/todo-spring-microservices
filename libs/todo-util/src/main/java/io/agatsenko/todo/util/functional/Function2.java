/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-25
 */
package io.agatsenko.todo.util.functional;

import io.agatsenko.todo.util.Check;

@FunctionalInterface
public interface Function2<A1, A2, R> {
    static <A1, A2, R> Function2<A1, A2, R> of(Function2<A1, A2, R> f) {
        return f;
    }

    R apply(A1 a1, A2 a2);

    default <R2> Function2<A1, A2, R2> andThen(Function1<? super R, ? extends R2> after) {
        Check.argNotNull(after, "after");
        return (A1 a1, A2 a2) -> after.apply(apply(a1, a2));
    }

    default CheckedFunction2<A1, A2, R, RuntimeException> toChecked() {
        return CheckedFunction2.of(this::apply);
    }
}
