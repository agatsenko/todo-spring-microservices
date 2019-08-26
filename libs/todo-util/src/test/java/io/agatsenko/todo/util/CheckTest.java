/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-25
 */
package io.agatsenko.todo.util;

import java.util.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CheckTest {
    @Test
    void shouldNotThrowAnyExceptionOnArgOfMsg() {
        Check.arg(true, () -> "error message");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionOnArgOfMsg() {
        final var message = "expected error message";
        assertThatThrownBy(() -> Check.arg(false, () -> message))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage(message);
    }

    @Test
    void shouldNotThrowAnyExceptionOnArgWithMsgFormat() {
        Check.arg(true, "message format, args: %s, %s", "arg1", "arg2");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionOnArgOfMsgFormat() {
        final var messageFormat = "expected error message, args: %s, %s";
        final var arg1 = "arg1";
        final var arg2 = "arg2";
        final var expectedMessage = String.format(messageFormat, arg1, arg2);

        assertThatThrownBy(() -> Check.arg(false, messageFormat, arg1, arg2))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage(expectedMessage);
    }

    @Test
    void shouldNotThrowAnyExceptionOnArgNotNull() {
        Check.argNotNull(new Object(), "arg");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionOnArgNotNull() {
        final var argName = "testArg";

        assertThatThrownBy(() -> Check.argNotNull(null, argName))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(argName);
    }

    static Stream<CharSequence> nonEmptyCharSequenceStream() {
        return Stream.of(
                "non empty string",
                new StringBuilder("non empty string builder")
        );
    }

    @ParameterizedTest
    @MethodSource("nonEmptyCharSequenceStream")
    void shouldNotThrowAnyExceptionOnArgNotEmptyOfCharSequence(CharSequence arg) {
        Check.argNotEmpty(arg, "testArg");
    }

    static Stream<CharSequence> nullOrEmptyCharSequenceStream() {
        return Stream.of(
                null,
                "",
                new StringBuilder()
        );
    }

    @ParameterizedTest
    @MethodSource("nullOrEmptyCharSequenceStream")
    void shouldThrowIllegalArgumentExceptionOnArgNotEmptyOfCharSequence(CharSequence arg) {
        final var argName = "testArg";

        assertThatThrownBy(() -> Check.argNotEmpty(arg, argName))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(argName);
    }

    @Test
    void shouldNotThrowAnyExceptionOnArgNotNullOeEmptyOfBooleanArray() {
        Check.argNotEmpty(new boolean[] {true, false}, "testArg");
    }

    static Stream<boolean[]> nullOrEmptyBooleanArrayStream() {
        return Stream.of(
                null,
                new boolean[0]
        );
    }

    @ParameterizedTest
    @MethodSource("nullOrEmptyBooleanArrayStream")
    void shouldThrowIllegalArgumentExceptionOnArgNotEmptyOfBooleanArray(boolean[] arg) {
        final var argName = "testArg";

        assertThatThrownBy(() -> Check.argNotEmpty(arg, argName))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(argName);
    }

    @Test
    void shouldNotThrowAnyExceptionOnArgNotNullOeEmptyOfByteArray() {
        Check.argNotEmpty(new byte[] {1, 2}, "testArg");
    }

    static Stream<byte[]> nullOrEmptyByteArrayStream() {
        return Stream.of(
                null,
                new byte[0]
        );
    }

    @ParameterizedTest
    @MethodSource("nullOrEmptyByteArrayStream")
    void shouldThrowIllegalArgumentExceptionOnArgNotEmptyOfByteArray(byte[] arg) {
        final var argName = "testArg";

        assertThatThrownBy(() -> Check.argNotEmpty(arg, argName))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(argName);
    }

    @Test
    void shouldNotThrowAnyExceptionOnArgNotNullOeEmptyOfShortArray() {
        Check.argNotEmpty(new short[] {1, 2}, "testArg");
    }

    static Stream<short[]> nullOrEmptyShortArrayStream() {
        return Stream.of(
                null,
                new short[0]
        );
    }

    @ParameterizedTest
    @MethodSource("nullOrEmptyShortArrayStream")
    void shouldThrowIllegalArgumentExceptionOnArgNotEmptyOfShortArray(short[] arg) {
        final var argName = "testArg";

        assertThatThrownBy(() -> Check.argNotEmpty(arg, argName))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(argName);
    }

    @Test
    void shouldNotThrowAnyExceptionOnArgNotNullOeEmptyOfIntArray() {
        Check.argNotEmpty(new int[] {1, 2}, "testArg");
    }

    static Stream<int[]> nullOrEmptyIntArrayStream() {
        return Stream.of(
                null,
                new int[0]
        );
    }

    @ParameterizedTest
    @MethodSource("nullOrEmptyIntArrayStream")
    void shouldThrowIllegalArgumentExceptionOnArgNotEmptyOfIntArray(int[] arg) {
        final var argName = "testArg";

        assertThatThrownBy(() -> Check.argNotEmpty(arg, argName))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(argName);
    }

    @Test
    void shouldNotThrowAnyExceptionOnArgNotNullOeEmptyOfLongArray() {
        Check.argNotEmpty(new long[] {1, 2}, "testArg");
    }

    static Stream<long[]> nullOrEmptyLongArrayStream() {
        return Stream.of(
                null,
                new long[0]
        );
    }

    @ParameterizedTest
    @MethodSource("nullOrEmptyLongArrayStream")
    void shouldThrowIllegalArgumentExceptionOnArgNotEmptyOfLongArray(long[] arg) {
        final var argName = "testArg";

        assertThatThrownBy(() -> Check.argNotEmpty(arg, argName))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(argName);
    }

    @Test
    void shouldNotThrowAnyExceptionOnArgNotNullOeEmptyOfFloatArray() {
        Check.argNotEmpty(new float[] {1, 2}, "testArg");
    }

    static Stream<float[]> nullOrEmptyFloatArrayStream() {
        return Stream.of(
                null,
                new float[0]
        );
    }

    @ParameterizedTest
    @MethodSource("nullOrEmptyFloatArrayStream")
    void shouldThrowIllegalArgumentExceptionOnArgNotEmptyOfFloatArray(float[] arg) {
        final var argName = "testArg";

        assertThatThrownBy(() -> Check.argNotEmpty(arg, argName))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(argName);
    }

    @Test
    void shouldNotThrowAnyExceptionOnArgNotNullOeEmptyOfDoubleArray() {
        Check.argNotEmpty(new double[] {1, 2}, "testArg");
    }

    static Stream<double[]> nullOrEmptyDoubleArrayStream() {
        return Stream.of(
                null,
                new double[0]
        );
    }

    @ParameterizedTest
    @MethodSource("nullOrEmptyDoubleArrayStream")
    void shouldThrowIllegalArgumentExceptionOnArgNotEmptyOfDoubleArray(double[] arg) {
        final var argName = "testArg";

        assertThatThrownBy(() -> Check.argNotEmpty(arg, argName))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(argName);
    }

    @Test
    void shouldNotThrowAnyExceptionOnArgNotNullOeEmptyOfCharArray() {
        Check.argNotEmpty(new char[] {'a', 'b'}, "testArg");
    }

    static Stream<char[]> nullOrEmptyCharArrayStream() {
        return Stream.of(
                null,
                new char[0]
        );
    }

    @ParameterizedTest
    @MethodSource("nullOrEmptyCharArrayStream")
    void shouldThrowIllegalArgumentExceptionOnArgNotEmptyOfCharArray(char[] arg) {
        final var argName = "testArg";

        assertThatThrownBy(() -> Check.argNotEmpty(arg, argName))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(argName);
    }

    @Test
    void shouldNotThrowAnyExceptionOnArgNotNullOeEmptyOfArray() {
        Check.argNotEmpty(new Object[] {"abc", 1}, "testArg");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionOnArgNotEmptyOfNullArray() {
        final var argName = "testArg";

        assertThatThrownBy(() -> Check.argNotEmpty((Object[]) null, argName))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(argName);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionOnArgNotEmptyOfEmptyArray() {
        final var arg = new Object[0];
        final var argName = "testArg";

        assertThatThrownBy(() -> Check.argNotEmpty(arg, argName))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(argName);
    }

    static Stream<Collection> nonEmptyCollectionStream() {
        return Stream.of(
                List.of("abc", 1),
                Set.of(1, "abc")
        );
    }

    @ParameterizedTest
    @MethodSource("nonEmptyCollectionStream")
    void shouldNotThrowAnyExceptionOnArgNotEmptyOfCollection(Collection arg) {
        Check.argNotEmpty(arg, "testArg");
    }

    static Stream<Collection> nullOrEmptyCollectionStream() {
        return Stream.of(
                null,
                Collections.emptyList(),
                Collections.emptySet()
        );
    }

    @ParameterizedTest
    @MethodSource("nullOrEmptyCollectionStream")
    void shouldThrowIllegalArgumentExceptionOnArgNotEmptyOfCollection(Collection arg) {
        final var argName = "testArg";

        assertThatThrownBy(() -> Check.argNotEmpty(arg, argName))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(argName);
    }

    @Test
    void shouldNotThrowAnyExceptionOnArgNotEmptyOfMap() {
        Check.argNotEmpty(Map.of("one", 1, "two", 2), "testArg");
    }

    static Stream<Map> nullOrEmptyMapStream() {
        return Stream.of(
                null,
                Collections.emptyMap()
        );
    }

    @ParameterizedTest
    @MethodSource("nullOrEmptyMapStream")
    void shouldThrowIllegalArgumentExceptionOnArgNotEmptyOfMap(Map arg) {
        final var argName = "testArg";

        assertThatThrownBy(() -> Check.argNotEmpty(arg, argName))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(argName);
    }

    @Test
    void shouldNotThrowAnyExceptionOnStateOfMsg() {
        Check.state(true, () -> "error message");
    }

    @Test
    void shouldThrowIllegalStateExceptionOnStateOfMsg() {
        final var message = "expected error message";
        assertThatThrownBy(() -> Check.state(false, () -> message))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage(message);
    }

    @Test
    void shouldNotThrowAnyExceptionOnStateWithMsgFormat() {
        Check.state(true, "message format, args: %s, %s", "arg1", "arg2");
    }

    @Test
    void shouldThrowIllegalStateExceptionOnStateOfMsgFormat() {
        final var messageFormat = "expected error message, args: %s, %s";
        final var arg1 = "arg1";
        final var arg2 = "arg2";
        final var expectedMessage = String.format(messageFormat, arg1, arg2);

        assertThatThrownBy(() -> Check.state(false, messageFormat, arg1, arg2))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage(expectedMessage);
    }
}
