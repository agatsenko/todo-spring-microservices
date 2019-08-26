/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-25
 */
package io.agatsenko.todo.util.functional;

@FunctionalInterface
public interface Procedure2<A1, A2> {
    static <A1, A2> Procedure2<A1, A2> of(Procedure2<A1, A2> p) {
        return p;
    }

    void apply(A1 a1, A2 a2);

    default CheckedProcedure2<A1, A2, RuntimeException> toChecked() {
        return CheckedProcedure2.of(this::apply);
    }
}
