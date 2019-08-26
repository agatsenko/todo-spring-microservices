/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-26
 */
package io.agatsenko.todo.util;

import java.util.Objects;
import java.util.Optional;

import io.agatsenko.todo.util.functional.*;

public interface Try<T> {
    static <T> Try<T> success(T value) {
        return new Success<>(value);
    }

    static <T> Try<T> failure(Throwable error) {
        return new Failure<>(error);
    }

    static <T> Try<T> apply(CheckedFunction0<T, ? extends Throwable> action) {
        Check.argNotNull(action, "action");
        try {
            return success(action.apply());
        }
        catch (Throwable t) {
            return Throwables.whenNonFatal(t, () -> failure(t));
        }
    }

    static Try<Void> apply(CheckedProcedure0<? extends Throwable> action) {
        Check.argNotNull(action, "action");
        return apply(() -> {
            action.apply();
            return null;
        });
    }

    static <T> Try<T> flatten(CheckedFunction0<Try<T>, ? extends Throwable> action) {
        Check.argNotNull(action, "action");
        try {
            return action.apply();
        }
        catch (Throwable t) {
            return Throwables.whenNonFatal(t, () -> failure(t));
        }
    }

    boolean isSuccess();

    boolean isFailure();

    T get() throws Throwable;

    <E extends Throwable> T get(CheckedFunction1<Throwable, T, E> failureMapper) throws E;

    T orElse(T other);

    T orElseGet(Function0<T> supplier);

    Optional<T> toOptional();

    void throwIfFailure() throws Throwable;

    <E extends Throwable> void throwIfFailure(CheckedFunction1<Throwable, E, E> mapper) throws E;

    <U> Try<U> map(CheckedFunction1<T, U, ? extends Throwable> mapper);

    <U> Try<U> flatMap(CheckedFunction1<T, Try<U>, ? extends Throwable> mapper);

    Try<T> recover(CheckedFunction1<Throwable, T, ? extends Throwable> recover);

    Try<T> flatRecover(CheckedFunction1<Throwable, Try<T>, ? extends Throwable> recover);

    <U> Try<U> transform(
            CheckedFunction1<T, U, ? extends Throwable> successMapper,
            CheckedFunction1<Throwable, U, ? extends Throwable> failureMapper);

    <U> Try<U> flatTransform(
            CheckedFunction1<T, Try<U>, ? extends Throwable> successMapper,
            CheckedFunction1<Throwable, Try<U>, ? extends Throwable> failureMapper);

    default Try<T> eventually(CheckedProcedure1<Try<T>, ? extends Throwable> action) {
        Check.argNotNull(action, "action");
        try {
            action.apply(this);
        }
        catch (Throwable t) {
            return Throwables.whenNonFatal(t, () -> {
                handleFailure(t::addSuppressed);
                return Try.failure(t);
            });
        }
        return this;
    }

    <E extends Throwable> Try<T> handleSuccess(CheckedProcedure1<T, E> handler) throws E;

    <E extends Throwable> Try<T> handleFailure(CheckedProcedure1<Throwable, E> handler) throws E;

    final class Success<T> implements Try<T> {
        private final T value;

        Success(T value) {
            this.value = value;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public boolean isFailure() {
            return false;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public <E extends Throwable> T get(CheckedFunction1<Throwable, T, E> failureMapper) {
            return value;
        }

        @Override
        public T orElse(T other) {
            return value;
        }

        @Override
        public T orElseGet(Function0<T> supplier) {
            return value;
        }

        @Override
        public Optional<T> toOptional() {
            return Optional.ofNullable(value);
        }

        @Override
        public void throwIfFailure() {
            // do nothing
        }

        @Override
        public <E extends Throwable> void throwIfFailure(CheckedFunction1<Throwable, E, E> mapper) {
            // do nothing
        }

        @Override
        public <U> Try<U> map(CheckedFunction1<T, U, ? extends Throwable> mapper) {
            Check.argNotNull(mapper, "mapper");
            return apply(() -> mapper.apply(value));
        }

        @Override
        public <U> Try<U> flatMap(CheckedFunction1<T, Try<U>, ? extends Throwable> mapper) {
            Check.argNotNull(mapper, "mapper");
            return flatten(() -> mapper.apply(value));
        }

        @Override
        public Try<T> recover(CheckedFunction1<Throwable, T, ? extends Throwable> recover) {
            return this;
        }

        @Override
        public Try<T> flatRecover(CheckedFunction1<Throwable, Try<T>, ? extends Throwable> recover) {
            return this;
        }

        @Override
        public <U> Try<U> transform(
                CheckedFunction1<T, U, ? extends Throwable> successMapper,
                CheckedFunction1<Throwable, U, ? extends Throwable> failureMapper) {
            Check.argNotNull(successMapper, "successMapper");
            return apply(() -> successMapper.apply(value));
        }

        @Override
        public <U> Try<U> flatTransform(
                CheckedFunction1<T, Try<U>, ? extends Throwable> successMapper,
                CheckedFunction1<Throwable, Try<U>, ? extends Throwable> failureMapper) {
            Check.argNotNull(successMapper, "successMapper");
            return flatten(() -> successMapper.apply(value));
        }

        @Override
        public <E extends Throwable> Try<T> handleSuccess(CheckedProcedure1<T, E> handler) throws E {
            Check.argNotNull(handler, "action");
            handler.apply(value);
            return this;
        }

        @Override
        public <E extends Throwable> Try<T> handleFailure(CheckedProcedure1<Throwable, E> handler) {
            return this;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != Success.class) {
                return false;
            }
            final Success success = (Success) obj;
            return Objects.equals(value, success.value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public String toString() {
            return "Success(" + value + ")";
        }
    }

    final class Failure<T> implements Try<T> {
        private final Throwable error;

        Failure(Throwable error) {
            Check.argNotNull(error, "error");
            this.error = error;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public boolean isFailure() {
            return true;
        }

        @Override
        public T get() throws Throwable {
            throw error;
        }

        @Override
        public <E extends Throwable> T get(CheckedFunction1<Throwable, T, E> failureMapper) throws E {
            Check.argNotNull(failureMapper, "failureMapper");
            return failureMapper.apply(error);
        }

        @Override
        public T orElse(T other) {
            return other;
        }

        @Override
        public T orElseGet(Function0<T> supplier) {
            Check.argNotNull(supplier, "supplier");
            return supplier.apply();
        }

        @Override
        public Optional<T> toOptional() {
            return Optional.empty();
        }

        @Override
        public void throwIfFailure() throws Throwable {
            throw error;
        }

        @Override
        public <E extends Throwable> void throwIfFailure(CheckedFunction1<Throwable, E, E> mapper) throws E {
            Check.argNotNull(mapper, "mapper");
            throw mapper.apply(error);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <U> Try<U> map(CheckedFunction1<T, U, ? extends Throwable> mapper) {
            return (Try<U>) this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <U> Try<U> flatMap(CheckedFunction1<T, Try<U>, ? extends Throwable> mapper) {
            return (Try<U>) this;
        }

        @Override
        public Try<T> recover(CheckedFunction1<Throwable, T, ? extends Throwable> recover) {
            Check.argNotNull(recover, "recover");
            try {
                return success(recover.apply(error));
            }
            catch (Throwable t) {
                return Throwables.whenNonFatal(t, () -> {
                    t.addSuppressed(error);
                    return failure(t);
                });
            }
        }

        @Override
        public Try<T> flatRecover(CheckedFunction1<Throwable, Try<T>, ? extends Throwable> recover) {
            Check.argNotNull(recover, "recover");
            try {
                return recover.apply(error);
            }
            catch (Throwable t) {
                return Throwables.whenNonFatal(t, () -> {
                    t.addSuppressed(error);
                    return failure(t);
                });
            }
        }

        @Override
        public <U> Try<U> transform(
                CheckedFunction1<T, U, ? extends Throwable> successMapper,
                CheckedFunction1<Throwable, U, ? extends Throwable> failureMapper) {
            Check.argNotNull(failureMapper, "failureMapper");
            try {
                return success(failureMapper.apply(error));
            }
            catch (Throwable t) {
                return Throwables.whenNonFatal(t, () -> {
                    t.addSuppressed(error);
                    return failure(t);
                });
            }
        }

        @Override
        public <U> Try<U> flatTransform(
                CheckedFunction1<T, Try<U>, ? extends Throwable> successMapper,
                CheckedFunction1<Throwable, Try<U>, ? extends Throwable> failureMapper) {
            Check.argNotNull(failureMapper, "failureMapper");
            try {
                return failureMapper.apply(error);
            }
            catch (Throwable t) {
                return Throwables.whenNonFatal(t, () -> {
                    t.addSuppressed(error);
                    return failure(t);
                });
            }
        }

        @Override
        public <E extends Throwable> Try<T> handleSuccess(CheckedProcedure1<T, E> handler) throws E {
            return this;
        }

        @Override
        public <E extends Throwable> Try<T> handleFailure(CheckedProcedure1<Throwable, E> handler) throws E {
            Check.argNotNull(handler, "action");
            handler.apply(error);
            return this;
        }

        @Override
        public String toString() {
            return "Failure(" + error.getMessage() + ")";
        }
    }
}
