/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-25
 */
package io.agatsenko.todo.util.functional;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CheckedFunction2Test {
    @Test
    void ofShouldCreateFunctionByLambda() {
        final var arg1 = 2;
        final var arg2 = 3;
        final var funcResult = arg1 + arg2;
        final var func = CheckedFunction2.of((Integer a1, Integer a2) -> a1 + a2);

        assertThat(func).isNotNull();
        assertThat(func.apply(arg1, arg2)).isEqualTo(funcResult);
    }

    @Test
    void ofShouldCreateFunctionByReference() {
        final var arg1 = 2;
        final var arg2 = 3;
        final var func = CheckedFunction2.of(Integer::sum);

        assertThat(func).isNotNull();
        assertThat(func.apply(arg1, arg2)).isEqualTo(Integer.sum(arg1, arg2));
    }

    @Test
    void andThenShouldReturnComposedFunction() {
        final var arg1 = 2;
        final var arg2 = 3;
        final var func = CheckedFunction2.of(Integer::sum);
        final var after = CheckedFunction1.of((Integer a) -> a * 4);
        final var expectedResult = after.apply(func.apply(arg1, arg2));

        final var composedFunc = func.andThen(after);
        assertThat(composedFunc).isNotNull();
        assertThat(composedFunc.apply(arg1, arg2)).isEqualTo(expectedResult);
    }

    @Test
    void andThenShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> CheckedFunction2.of((a1, a2) -> 1).andThen(null))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
