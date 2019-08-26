/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-25
 */
package io.agatsenko.todo.util.functional;

@FunctionalInterface
public interface Procedure0 {
    static Procedure0 of(Procedure0 p) {
        return p;
    }

    void apply();

    default CheckedProcedure0<RuntimeException> toChecked() {
        return CheckedProcedure0.of(this::apply);
    }
}
