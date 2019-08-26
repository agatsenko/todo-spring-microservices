/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-25
 */
package io.agatsenko.todo.util.functional;

@FunctionalInterface
public interface Procedure1<A1> {
    static <A1> Procedure1<A1> of(Procedure1<A1> p) {
        return p;
    }

    void apply(A1 a1) throws RuntimeException;

    default CheckedProcedure1<A1, RuntimeException> toChecked() {
        return CheckedProcedure1.of(this::apply);
    }
}
