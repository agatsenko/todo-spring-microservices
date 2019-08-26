/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-25
 */
package io.agatsenko.todo.util.functional;

import org.junit.jupiter.api.Test;

import io.agatsenko.todo.util.Var;

import static org.assertj.core.api.Assertions.assertThat;

public class Procedure1Test {
    static int sumFunc(int a1, int a2) {
        return a1 + a2;
    }

    @Test
    void ofShouldCreateFunctionByLambda() {
        final var var = Var.of(10);
        final var arg = 20;
        final var expectedVarValue = sumFunc(var.get(), arg);

        final var proc = Procedure1.of((Integer a) -> var.set(sumFunc(var.get(), a)));
        assertThat(proc).isNotNull();

        proc.apply(arg);
        assertThat(var.get()).isEqualTo(expectedVarValue);
    }

    final Var<Integer> instaceVar = Var.newEmpty();

    void instanceProc(int a) {
        instaceVar.set(sumFunc(instaceVar.get(), a));
    }

    @Test
    void ofShouldCreateFunctionByReference() {
        final var initVarValue = 10;
        final var arg = 20;
        final var expectedNewVarValue = sumFunc(initVarValue, arg);

        final var proc = Procedure1.of(this::instanceProc);
        assertThat(proc).isNotNull();

        instaceVar.set(initVarValue);
        proc.apply(arg);
        assertThat(instaceVar.get()).isEqualTo(expectedNewVarValue);
    }

    @Test
    void toCheckedShouldConvertThisProcedureToCheckedProcedure() {
        final var initVarValue = 10;
        final var arg = 20;

        final var var = Var.<Integer>newEmpty();

        final var proc = Procedure1.of((Integer a) -> var.set(sumFunc(var.get(), a)));
        CheckedProcedure1<Integer, RuntimeException> checkedProc = proc.toChecked();
        assertThat(checkedProc).isNotNull();

        var.set(initVarValue);
        proc.apply(arg);
        final var expectedVarValue = var.get();

        var.set(initVarValue);
        checkedProc.apply(arg);
        assertThat(var.get()).isEqualTo(expectedVarValue);
    }
}
