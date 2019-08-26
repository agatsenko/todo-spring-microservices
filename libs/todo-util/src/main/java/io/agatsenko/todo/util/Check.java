/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-25
 */
package io.agatsenko.todo.util;

import java.util.Collection;
import java.util.Map;

import lombok.experimental.UtilityClass;

import io.agatsenko.todo.util.functional.Function0;
import io.agatsenko.todo.util.functional.Function1;

@UtilityClass
public class Check {
    public static void arg(boolean condition, Function0<CharSequence> msg) {
        check(condition, msg, IllegalArgumentException::new);
    }

    public static void arg(boolean condition, CharSequence msgFormat, Object... msgArgs) {
        arg(condition, () -> String.format(msgFormat.toString(), msgArgs));
    }

    public static void argNotNull(Object arg, CharSequence argName) {
        arg(arg != null, () -> String.format("%s is null", argName));
    }

    public static void argNotEmpty(CharSequence arg, CharSequence argName) {
        arg(arg != null && arg.length() > 0, () -> String.format("%s is null or empty", argName));
    }

    public static void argNotEmpty(boolean[] arg, CharSequence argName) {
        arg(arg != null && arg.length > 0, () -> String.format("%s is null or empty", argName));
    }

    public static void argNotEmpty(byte[] arg, CharSequence argName) {
        arg(arg != null && arg.length > 0, () -> String.format("%s is null or empty", argName));
    }

    public static void argNotEmpty(short[] arg, CharSequence argName) {
        arg(arg != null && arg.length > 0, () -> String.format("%s is null or empty", argName));
    }

    public static void argNotEmpty(int[] arg, CharSequence argName) {
        arg(arg != null && arg.length > 0, () -> String.format("%s is null or empty", argName));
    }

    public static void argNotEmpty(long[] arg, CharSequence argName) {
        arg(arg != null && arg.length > 0, () -> String.format("%s is null or empty", argName));
    }

    public static void argNotEmpty(float[] arg, CharSequence argName) {
        arg(arg != null && arg.length > 0, () -> String.format("%s is null or empty", argName));
    }

    public static void argNotEmpty(double[] arg, CharSequence argName) {
        arg(arg != null && arg.length > 0, () -> String.format("%s is null or empty", argName));
    }

    public static void argNotEmpty(char[] arg, CharSequence argName) {
        arg(arg != null && arg.length > 0, () -> String.format("%s is null or empty", argName));
    }

    public static <T> void argNotEmpty(T[] arg, CharSequence argName) {
        arg(arg != null && arg.length > 0, () -> String.format("%s is null or empty", argName));
    }

    public static <T> void argNotEmpty(Collection<T> arg, CharSequence argName) {
        arg(arg != null && !arg.isEmpty(), () -> String.format("%s is null or empty", argName));
    }

    public static <K, V> void argNotEmpty(Map<K, V> arg, CharSequence argName) {
        arg(arg != null && !arg.isEmpty(), () -> String.format("%s is null or empty", argName));
    }

    public static void state(boolean condition, Function0<CharSequence> msg) {
        check(condition, msg, IllegalStateException::new);
    }

    public static void state(boolean condition, CharSequence msgFormat, Object... msgArgs) {
        state(condition, () -> String.format(msgFormat.toString(), msgArgs));
    }

    private static <E extends RuntimeException> void check(
            boolean condition,
            Function0<CharSequence> msg,
            Function1<String, E> ex) {
        if (!condition) {
            throw ex.apply(msg.apply().toString());
        }
    }
}
