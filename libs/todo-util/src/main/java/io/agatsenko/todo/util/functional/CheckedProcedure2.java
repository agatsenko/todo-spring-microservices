/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-25
 */
package io.agatsenko.todo.util.functional;

@FunctionalInterface
public interface CheckedProcedure2<A1, A2, E extends Throwable> {
    static <A1, A2, E extends Throwable> CheckedProcedure2<A1, A2, E> of(CheckedProcedure2<A1, A2, E> p) {
        return p;
    }

    void apply(A1 a1, A2 a2) throws E;
}
