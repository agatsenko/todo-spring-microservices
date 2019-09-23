/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-22
 */
package io.agatsenko.todo.util.functional;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
public final class Tuple1<T1> {
    public final T1 _1;

    @Override
    public String toString() {
        return "Tuple(" + _1 + ")";
    }
}
