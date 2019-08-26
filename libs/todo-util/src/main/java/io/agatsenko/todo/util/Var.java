/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-25
 */
package io.agatsenko.todo.util;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public final class Var<T> {
    private T value;

    public static <T> Var<T> of(T value) {
        return new Var<>(value);
    }

    public static <T> Var<T> newEmpty() {
        return new Var<>();
    }

    public boolean isEmpty() {
        return value == null;
    }

    public boolean isNonEmpty() {
        return !isEmpty();
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    public void clear() {
        set(null);
    }

    public Var<T> copy() {
        return new Var<>(value);
    }
}
