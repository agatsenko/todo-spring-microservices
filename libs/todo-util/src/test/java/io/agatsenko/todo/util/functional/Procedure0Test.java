/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-25
 */
package io.agatsenko.todo.util.functional;

import org.junit.jupiter.api.Test;

import io.agatsenko.todo.util.Var;

import static org.assertj.core.api.Assertions.assertThat;

public class Procedure0Test {
    @Test
    void ofShouldCreateFunctionByLambda() {
        final var var = Var.of(10);
        final var expectedVarValue = var.get() * 2;

        final var proc = Procedure0.of(() -> var.set(expectedVarValue));
        assertThat(proc).isNotNull();

        proc.apply();
        assertThat(var.get()).isEqualTo(expectedVarValue);
    }

    final Var<Integer> instaceVar = Var.newEmpty();
    final int newValueOfInsaceVar = 10;

    void instanceProc() {
        instaceVar.set(newValueOfInsaceVar);
    }

    @Test
    void ofShouldCreateFunctionByReference() {
        instaceVar.clear();

        final var proc = Procedure0.of(this::instanceProc);
        assertThat(proc).isNotNull();

        proc.apply();
        assertThat(instaceVar.get()).isEqualTo(newValueOfInsaceVar);
    }

    @Test
    void toCheckedShouldConvertThisProcedureToCheckedProcedure() {
        final var proc = Procedure0.of(() -> {
            // do nothing
        });
        CheckedProcedure0<RuntimeException> checkedProc = proc.toChecked();
        assertThat(checkedProc).isNotNull();
    }
}
