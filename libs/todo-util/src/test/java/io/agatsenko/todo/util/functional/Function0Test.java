/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-25
 */
package io.agatsenko.todo.util.functional;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Function0Test {
    @Test
    void ofShouldCreateFunctionByLambda() {
        final var funcResult = 1;
        final var func = Function0.of(() -> funcResult);
        assertThat(func).isNotNull();
        assertThat(func.apply()).isEqualTo(funcResult);
    }

    int instanceFunc() {
        return 1;
    }

    @Test
    void ofShouldCreateFunctionByReference() {
        final var func = Function0.of(this::instanceFunc);
        assertThat(func).isNotNull();
        assertThat(func.apply()).isEqualTo(instanceFunc());
    }

    @Test
    void andThenShouldReturnComposedFunction() {
        final var funcResult = 2;
        final var func = Function0.of(() -> funcResult);
        final var after = Function1.of((Integer a) -> a * 3);
        final var expectedResult = after.apply(funcResult);

        final var composedFunc = func.andThen(after);
        assertThat(composedFunc).isNotNull();
        assertThat(composedFunc.apply()).isEqualTo(expectedResult);
    }

    @Test
    void andThenShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> Function0.of(() -> 1).andThen(null))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void toCheckedShouldConvertThisFunctionToCheckedFunction() {
        final var func = Function0.of(() -> 1);
        CheckedFunction0<Integer, RuntimeException> checkedFunc = func.toChecked();
        assertThat(checkedFunc).isNotNull();
        assertThat(checkedFunc.apply()).isEqualTo(func.apply());
    }
}
