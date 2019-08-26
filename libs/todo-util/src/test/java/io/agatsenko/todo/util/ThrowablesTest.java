/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-26
 */
package io.agatsenko.todo.util;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ThrowablesTest {
    static Stream<Throwable> fatalThrowableStream() {
        return FatalErrors.fatalErrorStream();
    }

    static Stream<Throwable> nonFatalThrowableStream() {
        return FatalErrors.nonFatalErrorStream();
    }

    @ParameterizedTest
    @MethodSource("fatalThrowableStream")
    void shouldReturnTrueOnIsFatal(Throwable t) {
        assertThat(Throwables.isFatal(t)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("nonFatalThrowableStream")
    void shouldReturnFalseOnIsFatal(Throwable t) {
        assertThat(Throwables.isFatal(t)).isFalse();
    }

    @ParameterizedTest
    @MethodSource("fatalThrowableStream")
    void shouldThrowErrorOnWhenNonFatalWithFunc(Throwable t) {
        Throwable caught = null;
        try {
            Throwables.<Boolean, Throwable>whenNonFatal(t, () -> true);
        }
        catch (Throwable error) {
            caught = error;
        }
        assertThat(caught).isNotNull();
        if (t instanceof Error || t instanceof RuntimeException) {
            assertThat(caught).isSameAs(t);
        }
        else {
            assertThat(caught.getCause()).isSameAs(t);
        }
    }

    @ParameterizedTest
    @MethodSource("nonFatalThrowableStream")
    void shouldExecActionOnWhenNonFatalWithFunc(Throwable t) {
        final var executed = Throwables.whenNonFatal(t, () -> true);
        assertThat(executed).isTrue();
    }

    @ParameterizedTest
    @MethodSource("nonFatalThrowableStream")
    void shouldThrowActionExceptionOnWhenNonFatalWithFunc(Throwable t) {
        TestException expected = new TestException();
        TestException cathed = null;
        try {
            Throwables.<Boolean, TestException>whenNonFatal(t, () -> {
                throw expected;
            });
        }
        catch (TestException error) {
            cathed = error;
        }
        assertThat(cathed).isSameAs(expected);
    }

    ///

    @ParameterizedTest
    @MethodSource("fatalThrowableStream")
    void shouldThrowErrorOnWhenNonFatalWithProc(Throwable t) {
        final var var = Var.<Object>newEmpty();
        Throwable caught = null;
        try {
            Throwables.<Throwable>whenNonFatal(t, () -> var.set("test"));
        }
        catch (Throwable error) {
            caught = error;
        }
        assertThat(var.isEmpty()).isTrue();
        assertThat(caught).isNotNull();
        if (t instanceof Error || t instanceof RuntimeException) {
            assertThat(caught).isSameAs(t);
        }
        else {
            assertThat(caught.getCause()).isSameAs(t);
        }
    }

    @ParameterizedTest
    @MethodSource("nonFatalThrowableStream")
    void shouldExecActionOnWhenNonFatalWithProc(Throwable t) throws Throwable {
        final var var = Var.<Object>newEmpty();
        Throwables.<Throwable>whenNonFatal(t, () -> var.set("test"));
        assertThat(var.isEmpty()).isFalse();
    }

    @ParameterizedTest
    @MethodSource("nonFatalThrowableStream")
    void shouldThrowActionExceptionOnWhenNonFatalWithProc(Throwable t) {
        final var var = Var.<Object>newEmpty();
        TestException expected = new TestException();
        TestException cathed = null;
        try {
            Throwables.whenNonFatal(t, () -> {
                var.set("test");
                throw expected;
            });
        }
        catch (TestException error) {
            cathed = error;
        }
        assertThat(var.isEmpty()).isFalse();
        assertThat(cathed).isSameAs(expected);
    }

    static class TestException extends Exception {
    }
}
