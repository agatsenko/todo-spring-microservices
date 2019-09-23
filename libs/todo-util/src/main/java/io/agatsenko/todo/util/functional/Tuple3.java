/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-22
 */
package io.agatsenko.todo.util.functional;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
public final class Tuple3<T1, T2, T3> {
    public final T1 _1;
    public final T2 _2;
    public final T3 _3;

    @Override
    public String toString() {
        return "Tuple(" + _1 + ", " + _2 + ", " + _3 + ")";
    }
}
