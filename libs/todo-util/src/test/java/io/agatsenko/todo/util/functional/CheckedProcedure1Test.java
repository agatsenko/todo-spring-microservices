/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-25
 */
package io.agatsenko.todo.util.functional;

import org.junit.jupiter.api.Test;

import io.agatsenko.todo.util.Var;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckedProcedure1Test {
    static int sumFunc(int a1, int a2) {
        return a1 + a2;
    }

    @Test
    void ofShouldCreateFunctionByLambda() {
        final var var = Var.of(10);
        final var arg = 20;
        final var expectedVarValue = sumFunc(var.get(), arg);

        final var proc = CheckedProcedure1.of((Integer a) -> var.set(sumFunc(var.get(), a)));
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

        final var proc = CheckedProcedure1.of(this::instanceProc);
        assertThat(proc).isNotNull();

        instaceVar.set(initVarValue);
        proc.apply(arg);
        assertThat(instaceVar.get()).isEqualTo(expectedNewVarValue);
    }
}
