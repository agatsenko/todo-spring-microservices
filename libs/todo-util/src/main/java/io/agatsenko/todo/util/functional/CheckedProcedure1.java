/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-25
 */
package io.agatsenko.todo.util.functional;

@FunctionalInterface
public interface CheckedProcedure1<A1, E extends Throwable> {
    static <A1, E extends Throwable> CheckedProcedure1<A1, E> of(CheckedProcedure1<A1, E> p) {
        return p;
    }

    void apply(A1 a1) throws E;
}
