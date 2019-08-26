/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-25
 */
package io.agatsenko.todo.util.functional;

@FunctionalInterface
public interface CheckedProcedure0<E extends Throwable> {
    static <E extends Throwable> CheckedProcedure0<E> of(CheckedProcedure0<E> p) {
        return p;
    }

    void apply() throws E;
}
