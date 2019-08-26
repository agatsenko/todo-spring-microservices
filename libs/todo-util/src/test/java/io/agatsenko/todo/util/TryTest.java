/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-26
 */
package io.agatsenko.todo.util;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TryTest {
    static Stream<Throwable> nonFatalErrors() {
        return FatalErrors.nonFatalErrorStream();
    }

    static Stream<Throwable> fatalErrors() {
        return FatalErrors.fatalErrorStream();
    }

    @Test
    public void shouldReturnSuccessOnSuccess() throws Throwable {
        final var value = new Object();
        final var actual = Try.success(value);
        assertThat(actual).isExactlyInstanceOf(Try.Success.class);
        assertThat(actual.get()).isEqualTo(value);
    }

    @Test
    void shouldReturnFailureOnFailure() {
        final var error = new TestException();
        final var actula = Try.failure(error);
        assertThat(actula).isExactlyInstanceOf(Try.Failure.class);
        assertThatThrownBy(actula::throwIfFailure).isSameAs(error);
    }

    @Test
    void shouldReturnSuccessOnApply() throws Throwable {
        final var value = new Object();
        final var actual = Try.apply(() -> value);
        assertThat(actual).isExactlyInstanceOf(Try.Success.class);
        assertThat(actual.get()).isEqualTo(value);
    }

    @ParameterizedTest
    @MethodSource("nonFatalErrors")
    void shouldReturnFailureOnApply(Throwable nonFatal) {
        final var actual = Try.apply(() -> {
            throw nonFatal;
        });
        assertThat(actual).isExactlyInstanceOf(Try.Failure.class);
        assertThatThrownBy(actual::throwIfFailure).isSameAs(nonFatal);
    }

    @ParameterizedTest
    @MethodSource("fatalErrors")
    void shouldThrowFatalOnApply(Throwable fatal) {
        Throwable caught = null;
        try {
            Try.apply(() -> {
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
    void shouldReturnSuccessOnFlatten() {
        final var value = new Object();
        final var success = Try.success(value);
        final var actual  = Try.flatten(() -> success);
        assertThat(actual).isSameAs(success);
    }

    @ParameterizedTest
    @MethodSource("nonFatalErrors")
    void shouldReturnFailureOnFlatten(Throwable nonFatal) {
        final var actual = Try.flatten(() -> {
            throw nonFatal;
        });
        assertThat(actual).isExactlyInstanceOf(Try.Failure.class);
        assertThatThrownBy(actual::throwIfFailure).isSameAs(nonFatal);
    }

    @ParameterizedTest
    @MethodSource("fatalErrors")
    void shouldThrowFatalOnFlatten(Throwable fatal) {
        Throwable caught = null;
        try {
            Try.flatten(() -> {
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

    private static class TestException extends Exception {
    }
}
