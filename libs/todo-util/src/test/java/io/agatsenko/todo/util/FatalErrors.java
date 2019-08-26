/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-28
 */
package io.agatsenko.todo.util;

import java.util.stream.Stream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FatalErrors {
    public static Stream<Throwable> nonFatalErrorStream() {
        return Stream.of(
                new Error(),
                new Exception(),
                new RuntimeException()
        );
    }

    public static Stream<Throwable> fatalErrorStream() {
        return Stream.of(
                new StackOverflowError(),
                new UnknownError(),
                new InternalError(),
                new OutOfMemoryError(),
                new InterruptedException(),
                new ThreadDeath(),
                new WrapFatalException(null)
        );
    }
}
