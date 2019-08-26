/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-26
 */
package io.agatsenko.todo.util;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.*;

public class SuccessTest {
    static Stream<Throwable> nonFatalErrors() {
        return FatalErrors.nonFatalErrorStream();
    }

    static Stream<Throwable> fatalErrors() {
        return FatalErrors.fatalErrorStream();
    }

    @Test
    void shouldReturnTrueOnIsSuccess() {
        final var success = Try.success(new Object());
        assertThat(success.isSuccess()).isTrue();
    }

    @Test
    void shouldReturnFalseOnIsFailure() {
        final var success = Try.success(new Object());
        assertThat(success.isFailure()).isFalse();
    }

    @Test
    void shouldReturnValueOnGet() throws Throwable {
        final var value = new Object();
        final var success = Try.success(value);
        final var actual = success.get();
        assertThat(actual).isEqualTo(value);
    }

    @Test
    void shouldReturnValueOnGetWithFailureMapper() {
        final var value = new Object();
        final var success = Try.success(value);
        final var actual = success.get(e -> new Object());
        assertThat(actual).isEqualTo(value);
    }

    @Test
    void shouldReturnValueOnOrElse() {
        final var value = new Object();
        final var success = Try.success(value);
        final var actual = success.orElse(new Object());
        assertThat(actual).isEqualTo(value);
    }

    @Test
    void shouldReturnValueOnOrElseGet() {
        final var value = new Object();
        final var success = Try.success(value);
        final var actual = success.orElseGet(Object::new);
        assertThat(actual).isEqualTo(value);
    }

    @Test
    void shouldReturnNonEmptyOnToOptional() {
        final var value = new Object();
        final var success = Try.success(value);
        final var actual = success.toOptional();
        assertThat(actual).isNotEmpty().hasValue(value);
    }

    @Test
    void shouldReturnEmptyOnToOptional() {
        final Object value = null;
        final var success = Try.success(value);
        final var actual = success.toOptional();
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldNotThrowAnyExceptionOnThrowIfFailure() throws Throwable {
        final var success = Try.success(new Object());
        success.throwIfFailure();
    }

    @Test
    void shouldNotThrowAnyExceptionOnThrowIfFailureWithMapper() throws Throwable {
        final var success = Try.success(new Object());
        success.throwIfFailure(e -> e);
    }

    @Test
    void shouldReturnMappedValueOnMap() throws Throwable {
        final var srcValue = new Object();
        final var newValue = "new value";
        final var success = Try.success(srcValue);
        final var actual = success.map(v -> {
            assertThat(v).isEqualTo(srcValue);
            return newValue;
        });
        assertThat(actual).isNotNull();
        assertThat(actual).isNotSameAs(success);
        assertThat(actual.isSuccess()).isTrue();
        assertThat(actual.get()).isEqualTo(newValue);
    }

    @ParameterizedTest
    @MethodSource("nonFatalErrors")
    void shouldReturnFailureOnMap(Throwable nonFatal) {
        final var value = new Object();
        final var success = Try.success(value);
        final var actual = success.map(v -> {
            assertThat(v).isEqualTo(value);
            throw nonFatal;
        });
        assertThat(actual).isNotNull();
        assertThat(actual).isNotSameAs(success);
        assertThat(actual.isFailure()).isTrue();
        assertThatThrownBy(() -> actual.throwIfFailure()).isSameAs(nonFatal);
    }

    @ParameterizedTest
    @MethodSource("fatalErrors")
    void shouldThrowFatalOnMap(Throwable fatal) {
        final var value = new Object();
        final var success = Try.success(value);
        Throwable caught = null;
        try {
            success.map(v -> {
                assertThat(v).isEqualTo(value);
                throw fatal;
            });
        }
        catch (Throwable t) {
            caught = t;
        }
        assertThat(caught).isNotNull();
        if (fatal instanceof Error || fatal instanceof RuntimeException) {
            assertThat(caught).isSameAs(fatal);
        }
        else {
            assertThat(caught.getCause()).isSameAs(fatal);
        }
    }

    @Test
    void shouldReturnMappedValueOnFlatMap() throws Throwable {
        final var srcValue = new Object();
        final var newValue = "new value";
        final var success = Try.success(srcValue);
        final var actual = success.flatMap(v -> {
            assertThat(v).isEqualTo(srcValue);
            return Try.success(newValue);
        });
        assertThat(actual).isNotNull();
        assertThat(actual).isNotSameAs(success);
        assertThat(actual.isSuccess()).isTrue();
        assertThat(actual.get()).isEqualTo(newValue);
    }

    @ParameterizedTest
    @MethodSource("nonFatalErrors")
    void shouldReturnFailureOnFlatMap(Throwable nonFatal) {
        final var value = new Object();
        final var success = Try.success(value);
        final var actual = success.flatMap(v -> {
            assertThat(v).isEqualTo(value);
            throw nonFatal;
        });
        assertThat(actual).isNotNull();
        assertThat(actual).isNotSameAs(success);
        assertThat(actual.isFailure()).isTrue();
        assertThatThrownBy(() -> actual.throwIfFailure()).isSameAs(nonFatal);
    }

    @ParameterizedTest
    @MethodSource("fatalErrors")
    void shouldThrowFatalOnFlatMap(Throwable fatal) {
        final var value = new Object();
        final var success = Try.success(value);
        Throwable caught = null;
        try {
            success.flatMap(v -> {
                assertThat(v).isEqualTo(value);
                throw fatal;
            });
        }
        catch (Throwable t) {
            caught = t;
        }
        assertThat(caught).isNotNull();
        if (fatal instanceof Error || fatal instanceof RuntimeException) {
            assertThat(caught).isSameAs(fatal);
        }
        else {
            assertThat(caught.getCause()).isSameAs(fatal);
        }
    }

    @Test
    void shouldReturnSameTryOnRecover() {
        final var success = Try.success(new Object());
        final var actual = success.recover(e -> {
            fail("unexpected call recover");
            return null;
        });
        assertThat(actual).isSameAs(success);
    }

    @Test
    void shouldReturnSameTryOnFlatRecover() {
        final var success = Try.success(new Object());
        final var actual = success.flatRecover(e -> {
            fail("unexpected call recover");
            return null;
        });
        assertThat(actual).isSameAs(success);
    }

    @Test
    void shouldReturnSucceedMappedValueOnTransform() throws Throwable {
        final var srcValue = new Object();
        final var newValue = "newValue";
        final var success = Try.success(srcValue);
        final var actual = success.transform(
                v -> {
                    assertThat(v).isEqualTo(srcValue);
                    return newValue;
                },
                e -> {
                    fail("unexpected call failureMapper");
                    return null;
                }
        );
        assertThat(actual).isNotNull();
        assertThat(actual.isSuccess()).isTrue();
        assertThat(actual).isNotSameAs(success);
        assertThat(actual.get()).isEqualTo(newValue);
    }

    @ParameterizedTest
    @MethodSource("nonFatalErrors")
    void shouldReturnFailureOnTransform(Throwable nonFatal) {
        final var value = new Object();
        final var success = Try.success(value);
        final var actual = success.transform(
                v -> {
                    assertThat(v).isEqualTo(value);
                    throw nonFatal;
                },
                e -> {
                    fail("unexpected call failureMapper");
                    return null;
                }
        );
        assertThat(actual).isNotNull();
        assertThat(actual.isFailure()).isTrue();
        assertThat(actual).isNotSameAs(success);
        actual.handleFailure(e -> assertThat(e).isSameAs(nonFatal));
    }

    @ParameterizedTest
    @MethodSource("fatalErrors")
    void shouldThrowFatalOnTransform(Throwable fatal) {
        final var value = new Object();
        final var success = Try.success(value);
        Throwable caught = null;
        try {
            success.transform(
                    v -> {
                        assertThat(v).isEqualTo(value);
                        throw fatal;
                    },
                    e -> {
                        fail("unexpected call failureMapper");
                        return null;
                    }
            );
        }
        catch (Throwable t) {
            caught = t;
        }
        assertThat(caught).isNotNull();
        if (fatal instanceof Error || fatal instanceof RuntimeException) {
            assertThat(caught).isSameAs(fatal);
        }
        else {
            assertThat(caught.getCause()).isSameAs(fatal);
        }
    }

    @Test
    void shouldReturnSucceedMappedValueOnFlatTransform() throws Throwable {
        final var srcValue = new Object();
        final var newValue = "newValue";
        final var success = Try.success(srcValue);
        final var actual = success.flatTransform(
                v -> {
                    assertThat(v).isEqualTo(srcValue);
                    return Try.success(newValue);
                },
                e -> {
                    fail("unexpected call failureMapper");
                    return null;
                }
        );
        assertThat(actual).isNotNull();
        assertThat(actual.isSuccess()).isTrue();
        assertThat(actual).isNotSameAs(success);
        assertThat(actual.get()).isEqualTo(newValue);
    }

    @ParameterizedTest
    @MethodSource("nonFatalErrors")
    void shouldReturnFailureOnFlatTransform(Throwable nonFatal) {
        final var value = new Object();
        final var success = Try.success(value);
        final var actual = success.flatTransform(
                v -> {
                    assertThat(v).isEqualTo(value);
                    throw nonFatal;
                },
                e -> {
                    fail("unexpected call failureMapper");
                    return null;
                }
        );
        assertThat(actual).isNotNull();
        assertThat(actual.isFailure()).isTrue();
        assertThat(actual).isNotSameAs(success);
        actual.handleFailure(e -> assertThat(e).isSameAs(nonFatal));
    }

    @ParameterizedTest
    @MethodSource("fatalErrors")
    void shouldThrowFatalOnFlatTransform(Throwable fatal) {
        final var value = new Object();
        final var success = Try.success(value);
        Throwable caught = null;
        try {
            success.flatTransform(
                    v -> {
                        assertThat(v).isEqualTo(value);
                        throw fatal;
                    },
                    e -> {
                        fail("unexpected call failureMapper");
                        return null;
                    }
            );
        }
        catch (Throwable t) {
            caught = t;
        }
        assertThat(caught).isNotNull();
        if (fatal instanceof Error || fatal instanceof RuntimeException) {
            assertThat(caught).isSameAs(fatal);
        }
        else {
            assertThat(caught.getCause()).isSameAs(fatal);
        }
    }

    @Test
    void shouldExecuteActionOnEventuallyAndReturnSameTry() {
        final var success = Try.success(new Object());
        final var eventuallyExecuted = Var.of(false);
        final var actual = success.eventually(tr -> {
            assertThat(tr).isSameAs(success);
            eventuallyExecuted.set(true);
        });
        assertThat(actual).isSameAs(success);
        assertThat(eventuallyExecuted.get()).isTrue();
    }

    @ParameterizedTest
    @MethodSource("nonFatalErrors")
    void shouldExecuteActionOnEventuallyAndReturnNewFailureIfActionThrowsNonFatal(Throwable nonFatal) {
        final var success = Try.success(new Object());
        final var eventuallyExecuted = Var.of(false);
        final var actual = success.eventually(tr -> {
            assertThat(tr).isSameAs(success);
            eventuallyExecuted.set(true);
            throw nonFatal;
        });
        assertThat(eventuallyExecuted.get()).isTrue();
        assertThat(actual).isNotNull();
        assertThat(actual.isFailure()).isTrue();
        actual.handleFailure(e -> assertThat(e).isSameAs(nonFatal));
    }

    @ParameterizedTest
    @MethodSource("fatalErrors")
    void shouldThrowFatalOnEventually(Throwable fatal) {
        final var success = Try.success(new Object());
        Throwable caught = null;
        try {
            success.eventually(tr -> {
                assertThat(tr).isSameAs(success);
                throw fatal;
            });
        }
        catch (Throwable t) {
            caught = t;
        }
        assertThat(caught).isNotNull();
        if (fatal instanceof Error || fatal instanceof RuntimeException) {
            assertThat(caught).isSameAs(fatal);
        }
        else {
            assertThat(caught.getCause()).isSameAs(fatal);
        }
    }

    @Test
    void shouldExecuteHandlerOnHandleSuccess() {
        final var value = new Object();
        final var success = Try.success(value);
        final var handlerExecuted = Var.of(false);
        final var actual = success.handleSuccess(v -> {
            assertThat(v).isEqualTo(value);
            handlerExecuted.set(true);
        });
        assertThat(actual).isSameAs(success);
        assertThat(handlerExecuted.get()).isTrue();
    }

    @Test
    void shouldThrowHandlerErrorOnHandleSuccess() {
        final var value = new Object();
        final var success = Try.success(value);
        final var error = new Exception();
        final var handlerExecuted = Var.of(false);
        assertThatThrownBy(() -> {
            success.handleSuccess(v -> {
                assertThat(v).isEqualTo(value);
                handlerExecuted.set(true);
                throw error;
            });
        }).isSameAs(error);
        assertThat(handlerExecuted.get()).isTrue();
    }

    @Test
    void shouldNotExecuteHandlerOnHandleFailure() {
        final var success = Try.success(new Object());
        final var handlerExecuted = Var.of(false);
        final var actual = success.handleFailure(e -> handlerExecuted.set(true));
        assertThat(actual).isSameAs(success);
        assertThat(handlerExecuted.get()).isFalse();
    }
}
