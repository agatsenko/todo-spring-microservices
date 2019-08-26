/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-26
 */
package io.agatsenko.todo.util;

import java.util.Objects;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.*;

public class FailureTest {
    static Stream<Throwable> nonFatalErrors() {
        return FatalErrors.nonFatalErrorStream();
    }

    static Stream<Throwable> fatalErrors() {
        return FatalErrors.fatalErrorStream();
    }

    @Test
    void shouldReturnFalseOnIsSuccess() {
        final var failure = Try.<Object>failure(new TestException());
        assertThat(failure.isSuccess()).isFalse();
    }

    @Test
    void shouldReturnTrueOnIsFailure() {
        final var failure = Try.<Object>failure(new TestException());
        assertThat(failure.isFailure()).isTrue();
    }

    @Test
    void shouldThrowErrorOnGet() {
        final var error = new TestException();
        final var failure = Try.<Object>failure(error);
        assertThatThrownBy(failure::get).isSameAs(error);
    }

    @Test
    void shouldReturnMappedValueOnGetWithFailureMapper() {
        final var error = new TestException();
        final var failure = Try.failure(error);
        final var mappedValue = new Object();
        final var actualValue = failure.get(e -> {
            assertThat(e).isSameAs(error);
            return mappedValue;
        });
        assertThat(actualValue).isSameAs(mappedValue);
    }

    @Test
    void shouldReturnOtherValueOnOrElse() {
        final var failure = Try.failure(new TestException());
        final var otherValue = new Object();
        final var actualValue = failure.orElse(otherValue);
        assertThat(actualValue).isSameAs(otherValue);
    }

    @Test
    void shouldReturnOtherValueOnOrElseGet() {
        final var failure = Try.failure(new TestException());
        final var otherValue = new Object();
        final var actualValue = failure.orElseGet(() -> otherValue);
        assertThat(actualValue).isSameAs(otherValue);
    }

    @Test
    void shouldReturnEmptyOnToOptional() {
        final var failure = Try.failure(new TestException());
        final var optional = failure.toOptional();
        assertThat(optional).isEmpty();
    }

    @Test
    void shouldThrowErrorOnThrowIfFailure() {
        final var error = new TestException();
        final var failure = Try.failure(error);
        assertThatThrownBy(failure::throwIfFailure).isSameAs(error);
    }

    @Test
    void shouldThrowOtherErrorOnThrowIfFailureWithMapper() {
        final var srcError = new TestException();
        final var failure = Try.failure(srcError);
        assertThatThrownBy(() -> failure.throwIfFailure(e -> new TestException(e)))
                .isNotSameAs(srcError)
                .hasCause(srcError);
    }

    @Test
    void shouldReturnSameFailureOnMap() {
        final var failure = Try.<Object>failure(new TestException());
        final var newTry = failure.map(Objects::toString);
        assertThat(newTry).isSameAs(failure);
    }

    @Test
    void shouldReturnSameFailureOnFlatMap() {
        final var failure = Try.<Object>failure(new TestException());
        final var actual = failure.flatMap(v -> Try.success(new Object()));
        assertThat(actual).isSameAs(failure);
    }

    @Test
    void shouldReturnSuccessWithRecoveredValueOnRecover() throws Throwable {
        final var error = new TestException();
        final var failure = Try.<Object>failure(error);
        final var recoveredValue = new Object();
        final var actual = failure.recover(e -> {
            assertThat(e).isSameAs(error);
            return recoveredValue;
        });
        assertThat(actual).isNotNull();
        assertThat(actual.isSuccess());
        assertThat(actual.get()).isSameAs(recoveredValue);
    }

    @ParameterizedTest
    @MethodSource("nonFatalErrors")
    void shouldReturnNewFailureOnRecoverIfRecoverThrowsNonFatal(Throwable nonFatal) {
        TestException srcError = new TestException();
        final var failure = Try.<Object>failure(srcError);
        final var actual = failure.recover(e -> {
            throw nonFatal;
        });
        assertThat(actual).isNotNull();
        assertThat(actual).isNotSameAs(failure);
        assertThat(actual.isFailure());
        assertThatThrownBy(() -> actual.throwIfFailure()).isSameAs(nonFatal).hasSuppressedException(srcError);
    }

    @ParameterizedTest
    @MethodSource("fatalErrors")
    void shouldThrowFatalOnRecover(Throwable fatal) {
        final var failure = Try.<Object>failure(new TestException());
        Throwable caught = null;
        try {
            failure.recover(e -> {
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
    void shouldReturnSuccessWithRecoveredValueOnFlatRecover() throws Throwable {
        final var error = new TestException();
        final var failure = Try.<Object>failure(error);
        final var recoveredValue = new Object();
        final var actual = failure.flatRecover(e -> {
            assertThat(e).isSameAs(error);
            return Try.success(recoveredValue);
        });
        assertThat(actual).isNotNull();
        assertThat(actual.isSuccess());
        assertThat(actual.get()).isSameAs(recoveredValue);
    }

    @ParameterizedTest
    @MethodSource("nonFatalErrors")
    void shouldReturnNewFailureOnFlatRecoverIfRecoverThrowsNonFatal(Throwable nonFatal) {
        TestException srcError = new TestException();
        final var failure = Try.<Object>failure(srcError);
        final var actual = failure.flatRecover(e -> {
            throw nonFatal;
        });
        assertThat(actual).isNotNull();
        assertThat(actual).isNotSameAs(failure);
        assertThat(actual.isFailure());
        assertThatThrownBy(() -> actual.throwIfFailure()).isSameAs(nonFatal).hasSuppressedException(srcError);
    }

    @ParameterizedTest
    @MethodSource("fatalErrors")
    void shouldThrowFatalOnFlatRecover(Throwable fatal) {
        final var failure = Try.<Object>failure(new TestException());
        Throwable caught = null;
        try {
            failure.flatRecover(e -> {
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
    void shouldReturnFailureMappedValueOnTransform() throws Throwable {
        final var error = new TestException();
        final var failure = Try.<Object>failure(error);
        final var recoveredValue = new Object();
        final var actual = failure.transform(
                v -> {
                    fail("unexpected call successMapper");
                    return null;
                },
                e -> {
                    assertThat(e).isSameAs(error);
                    return recoveredValue;
                }
        );
        assertThat(actual).isNotNull();
        assertThat(actual.isSuccess());
        assertThat(actual.get()).isSameAs(recoveredValue);
    }

    @ParameterizedTest
    @MethodSource("nonFatalErrors")
    void shouldReturnNewFailureOnTransformIfFailureMapperThrowsNonFatal(Throwable nonFatal) {
        TestException srcError = new TestException();
        final var failure = Try.<Object>failure(srcError);
        final var actual = failure.transform(
                v -> {
                    fail("unexpected call successMapper");
                    return null;
                },
                e -> {
                    throw nonFatal;
                }
        );
        assertThat(actual).isNotNull();
        assertThat(actual).isNotSameAs(failure);
        assertThat(actual.isFailure());
        assertThatThrownBy(() -> actual.throwIfFailure()).isSameAs(nonFatal).hasSuppressedException(srcError);
    }

    @ParameterizedTest
    @MethodSource("fatalErrors")
    void shouldThrowFatalOnTransform(Throwable fatal) {
        final var failure = Try.<Object>failure(new TestException());
        Throwable caught = null;
        try {
            failure.transform(
                    v -> null,
                    e -> {
                        throw fatal;
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
    void shouldReturnFailureMappedValueOnFlatTransform() throws Throwable {
        final var error = new TestException();
        final var failure = Try.<Object>failure(error);
        final var recoveredValue = new Object();
        final var actual = failure.flatTransform(
                v -> {
                    fail("unexpected call successMapper");
                    return null;
                },
                e -> {
                    assertThat(e).isSameAs(error);
                    return Try.success(recoveredValue);
                }
        );
        assertThat(actual).isNotNull();
        assertThat(actual.isSuccess());
        assertThat(actual.get()).isSameAs(recoveredValue);
    }

    @ParameterizedTest
    @MethodSource("nonFatalErrors")
    void shouldReturnNewFailureOnFlatTransformIfFailureMapperThrowsNonFatal(Throwable nonFatal) {
        TestException srcError = new TestException();
        final var failure = Try.<Object>failure(srcError);
        final var actual = failure.flatTransform(
                v -> null,
                e -> {
                    throw nonFatal;
                }
        );
        assertThat(actual).isNotNull();
        assertThat(actual).isNotSameAs(failure);
        assertThat(actual.isFailure());
        assertThatThrownBy(() -> actual.throwIfFailure()).isSameAs(nonFatal).hasSuppressedException(srcError);
    }

    @ParameterizedTest
    @MethodSource("fatalErrors")
    void shouldThrowFatalOnFlatTransform(Throwable fatal) {
        final var failure = Try.<Object>failure(new TestException());
        Throwable caught = null;
        try {
            failure.flatTransform(
                    v -> null,
                    e -> {
                        throw fatal;
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
        final var failure = Try.<Object>failure(new TestException());
        final var eventuallyCalled = Var.of(false);
        final var actual = failure.eventually(tr -> {
            assertThat(tr).isSameAs(failure);
            eventuallyCalled.set(true);
        });
        assertThat(actual).isSameAs(failure);
        assertThat(eventuallyCalled.get()).isTrue();
    }

    @ParameterizedTest
    @MethodSource("nonFatalErrors")
    void shouldExecuteActionOnEventuallyAndReturnNewFailureIfActionThrowsNonFatal(Throwable nonFatal) {
        final var srcError = new TestException();
        final var failure = Try.failure(srcError);
        final var eventuallyCalled = Var.of(false);
        final var actual = failure.eventually(tr -> {
            assertThat(tr).isSameAs(failure);
            eventuallyCalled.set(true);
            throw nonFatal;
        });
        assertThat(actual).isNotNull();
        assertThat(actual).isNotSameAs(failure);
        assertThat(actual.isFailure()).isTrue();
        assertThat(eventuallyCalled.get()).isTrue();
        assertThatThrownBy(() -> actual.throwIfFailure()).isSameAs(nonFatal).hasSuppressedException(srcError);
    }

    @ParameterizedTest
    @MethodSource("fatalErrors")
    void shouldThrowFatalOnEventually(Throwable fatal) {
        final var failure = Try.<Object>failure(new TestException());
        Throwable caught = null;
        try {
            failure.eventually(
                    tr -> {
                        throw fatal;
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
    void shouldNotExecuteHandlerOnHandleSuccess() {
        final var failure = Try.failure(new TestException());
        final var handlerCalled = Var.of(false);
        final var actual = failure.handleSuccess(e -> handlerCalled.set(true));
        assertThat(actual).isSameAs(failure);
        assertThat(handlerCalled.get()).isFalse();
    }

    @Test
    void shouldExecuteHandlerOnHandleFailure() {
        final var failure = Try.failure(new TestException());
        final var handlerCalled = Var.of(false);
        final var actual = failure.handleFailure(e -> handlerCalled.set(true));
        assertThat(actual).isSameAs(failure);
        assertThat(handlerCalled.get()).isTrue();
    }

    @Test
    void shouldThrowHandlerErrorOnHandleFailure() {
        final var srcError = new TestException();
        final var handlerError = new TestException();
        final var failure = Try.failure(srcError);
        assertThatThrownBy(() -> failure.handleFailure(e -> {
            throw handlerError;
        })).isNotSameAs(srcError).hasNoSuppressedExceptions();
    }

    private static class TestException extends Exception {
        public TestException() {
        }

        public TestException(Throwable cause) {
            super(cause);
        }
    }
}
