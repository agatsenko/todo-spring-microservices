/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-25
 */
package io.agatsenko.todo.util.functional;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CheckedFunction1Test {
    @Test
    void ofShouldCreateFunctionByLambda() {
        final var funcArg = 2;
        final var funcResult = funcArg + 3;
        final var func = CheckedFunction1.of((Integer a) -> a + 3);

        assertThat(func).isNotNull();
        assertThat(func.apply(funcArg)).isEqualTo(funcResult);
    }

    int instanceFunc(int arg) {
        return arg + 3;
    }

    @Test
    void ofShouldCreateFunctionByReference() {
        final var arg = 2;
        final var func = CheckedFunction1.of(this::instanceFunc);

        assertThat(func).isNotNull();
        assertThat(func.apply(arg)).isEqualTo(instanceFunc(arg));
    }

    @Test
    void composeShouldReturnComposedFunction() {
        final var funcArg = 2;
        final var func = CheckedFunction1.of(this::instanceFunc);
        final var before = CheckedFunction1.of((Integer a) -> a * 4);
        final var expectedResult = func.apply(before.apply(funcArg));

        final var composedFunc = func.compose(before);
        assertThat(composedFunc).isNotNull();
        assertThat(composedFunc.apply(funcArg)).isEqualTo(expectedResult);
    }

    @Test
    void composeShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> CheckedFunction1.of(a -> 1).compose(null))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void andThenShouldReturnComposedFunction() {
        final var funcArg = 2;
        final var func = CheckedFunction1.of(this::instanceFunc);
        final var after = CheckedFunction1.of((Integer a) -> a * 4);
        final var expectedResult = after.apply(func.apply(funcArg));

        final var composedFunc = func.andThen(after);
        assertThat(composedFunc).isNotNull();
        assertThat(composedFunc.apply(funcArg)).isEqualTo(expectedResult);
    }

    @Test
    void andThenShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> CheckedFunction1.of(a -> 1).andThen(null))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
