/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-25
 */
package io.agatsenko.todo.util.functional;

import org.junit.jupiter.api.Test;

import io.agatsenko.todo.util.Var;

import static org.assertj.core.api.Assertions.assertThat;

public class Procedure2Test {
    static int sumFunc(int a1, int a2, int a3) {
        return a1 + a2 + a3;
    }

    @Test
    void ofShouldCreateFunctionByLambda() {
        final var var = Var.of(10);
        final var arg1 = 20;
        final var arg2 = 30;
        final var expectedVarValue = sumFunc(var.get(), arg1, arg2);

        final var proc = Procedure2.of((Integer a1, Integer a2) -> var.set(sumFunc(var.get(), a1, a2)));
        assertThat(proc).isNotNull();

        proc.apply(arg1, arg2);
        assertThat(var.get()).isEqualTo(expectedVarValue);
    }

    final Var<Integer> instaceVar = Var.newEmpty();

    void instanceProc(int a1, int a2) {
        instaceVar.set(sumFunc(instaceVar.get(), a1, a2));
    }

    @Test
    void ofShouldCreateFunctionByReference() {
        final var initVarValue = 10;
        final var arg1 = 20;
        final var arg2 = 30;
        final var expectedNewVarValue = sumFunc(initVarValue, arg1, arg2);

        final var proc = Procedure2.of(this::instanceProc);
        assertThat(proc).isNotNull();

        instaceVar.set(initVarValue);
        proc.apply(arg1, arg2);
        assertThat(instaceVar.get()).isEqualTo(expectedNewVarValue);
    }

    @Test
    void toCheckedShouldConvertThisProcedureToCheckedProcedure() {
        final var initVarValue = 10;
        final var arg1 = 20;
        final var arg2 = 30;

        final var var = Var.<Integer>newEmpty();

        final var proc = Procedure2.of((Integer a1, Integer a2) -> var.set(sumFunc(var.get(), a1, a2)));
        CheckedProcedure2<Integer, Integer, RuntimeException> checkedProc = proc.toChecked();
        assertThat(checkedProc).isNotNull();

        var.set(initVarValue);
        proc.apply(arg1, arg2);
        final var expectedVarValue = var.get();

        var.set(initVarValue);
        checkedProc.apply(arg1, arg2);
        assertThat(var.get()).isEqualTo(expectedVarValue);
    }
}
